package com.betafore.evoting.OrganizerManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.RolePermissionManagement.Permission;
import com.betafore.evoting.RolePermissionManagement.Role;
import com.betafore.evoting.RolePermissionManagement.RolePermissionService;
import com.betafore.evoting.RolePermissionManagement.RoleRepository;
import com.betafore.evoting.security_config.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizerService {

    private final OrganizerRepository organizerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ExpoRepository expoRepository;
    private final RoleRepository roleRepository;
    private final MyUserDetailsService userDetailsService;
    private final RolePermissionService rolePermissionService;

    public OrganizerDto findByEmail(String email) throws CustomException {
        Organizer organizer = organizerRepository.findOrganizerByEmail(email).orElseThrow(() -> new CustomException("Organizer not found by given mail"));
        return entityToDto(organizer);
    }

    @Transactional
    public void createSuperAdmin(OrganizerDto request, String role) {
        Optional<Organizer> optionalOrganizer = organizerRepository.findOrganizerByEmail(request.getEmail());
        if (optionalOrganizer.isPresent()) {
            return;
        }
        rolePermissionService.saveAllPermission();
        Set<Permission> permissionSet = new HashSet<>(rolePermissionService.getAllPermission());
        Role superAdmin = Role.builder().expoId(0L).name(role).build();
        superAdmin.addAllPermission(permissionSet);
        roleRepository.save(superAdmin);
        Organizer organizer = Organizer.builder()
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(Boolean.parseBoolean(request.getEnabled()))
            .expoId(0L)
            .build();
        organizer.addRole(superAdmin);
        organizerRepository.save(organizer);
    }

    public OrganizerDto save(OrganizerDto request, Long expoId) throws CustomException {

        checkValidExpoId(expoId);
        Optional<Organizer> optionalOrganizer = organizerRepository.findByEmailAndExpoId(request.getEmail(), expoId);
        if (optionalOrganizer.isPresent()) {
            throw new CustomException("Email already taken");
        }

        Optional<Organizer> organizerByPhoneNumberAndExpoId = organizerRepository.findOrganizerByPhoneNumberAndExpoId(request.getPhoneNumber(), expoId);
        if (organizerByPhoneNumberAndExpoId.isPresent()) {
            throw new CustomException("Phone number already taken");
        }
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() ->
            new CustomException("Role not found"));

        Organizer organizer = Organizer.builder()
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(Boolean.parseBoolean(request.getEnabled()))
            .expoId(expoId)
            .build();
        organizer.addRole(role);
        Organizer saved = organizerRepository.save(organizer);
        return entityToDto(saved);
    }

    private OrganizerDto entityToDto(Organizer organizer) {
        Role role = organizer.getRoles().stream().findFirst().get();
        return OrganizerDto.builder()
            .id(organizer.getId())
            .phoneNumber(organizer.getPhoneNumber())
            .email(organizer.getEmail())
            .password(organizer.getPassword())
            .enabled(String.valueOf(organizer.isEnabled()))
            .expoId(organizer.getExpoId())
            .roleId(role.getId())
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, Long expoId) throws CustomException {
        Optional<Organizer> byExpoId = organizerRepository.findByEmailAndExpoId(request.getEmail(), expoId);
        if (byExpoId.isEmpty()) {
            throw new CustomException("Organizer not found in this expo");
        }
        if (expoId > 0) {
            checkValidExpoId(expoId);
        }
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        Organizer organizerByExpoId = byExpoId.get();
        var organizer = userDetailsService.loadUserByUsername(request.getEmail());
        var jwtToken = jwtService.generateToken(organizer);
        var refreshToken = jwtService.generateRefreshToken(organizer);
        revokeAllUserTokens(organizerByExpoId);
        saveUserToken(organizerByExpoId, jwtToken);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    public AuthenticationResponse authenticateByPhoneNumber(String phoneNumber, Long expoId) throws CustomException {
        Optional<Organizer> organizerByPhoneNumberAndExpoId = organizerRepository.findOrganizerByPhoneNumberAndExpoId(phoneNumber, expoId);
        if (organizerByPhoneNumberAndExpoId.isEmpty()) {
            throw new CustomException("The requested phone number not found in this expo");
        }
        if (expoId > 0) {
            checkValidExpoId(expoId);
        }
        var userDetails = userDetailsService.loadUserByUsername(phoneNumber);
        Organizer organizer = organizerByPhoneNumberAndExpoId.get();
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        revokeAllUserTokens(organizer);
        saveUserToken(organizer, jwtToken);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    public OrganizerDto findById(Long id) throws CustomException {

        Organizer organizer = organizerRepository.findById(id).orElseThrow(() -> new CustomException("User not found: " + id));

        return entityToDto(organizer);
    }

    public List<OrganizerDto> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<Organizer> allByExpoId = organizerRepository.findAllByExpoId(expoId);
        return allByExpoId.stream().map(this::entityToDto)
            .collect(Collectors.toList());
    }

    public Role getAllRoleByOrganizerId(Long id) throws CustomException {
        Organizer organizer = organizerRepository.findById(id).orElseThrow(() -> new CustomException("Organizer not found"));
        Collection<Role> roles = organizer.getRoles();
        if (roles.isEmpty()) return null;
        return roles.stream().findFirst().get();
    }


    @Transactional
    public void update(Long id, OrganizerDto organizerDto) throws CustomException {
        Optional<Organizer> optionalOrganizer = organizerRepository.findById(id);

        if (optionalOrganizer.isEmpty()) throw new CustomException("user not found by given id: ");

        Organizer organizer = optionalOrganizer.get();

        if (organizerDto.getEmail() != null && !organizerDto.getEmail().isEmpty() && !Objects.equals(organizer.getEmail(), organizerDto.getEmail())) {
            Optional<Organizer> byEmail = organizerRepository.findOrganizerByEmail(organizerDto.getEmail());
            if (byEmail.isPresent()) {
                throw new CustomException("email already taken. please try different");
            }
            organizer.setEmail(organizerDto.getEmail());
        }

        if (organizerDto.getPhoneNumber() != null && !organizerDto.getPhoneNumber().isEmpty()
            && !Objects.equals(organizer.getPhoneNumber(), organizerDto.getPhoneNumber())) {
            Optional<Organizer> byEmail = organizerRepository.findByPhoneNumber(organizerDto.getPhoneNumber());
            if (byEmail.isPresent()) {
                throw new CustomException("phone number already taken. please try different");
            }
            organizer.setPhoneNumber(organizerDto.getPhoneNumber());
        }

        if (organizerDto.getPassword() != null && !organizerDto.getPassword().isEmpty()) {
            organizer.setPassword(passwordEncoder.encode(organizerDto.getPassword()));
        }

        if (organizerDto.getRoleId() != null && !organizer.getRoles().stream().findFirst().get().getId().equals(organizerDto.getRoleId())) {
            Role role = roleRepository.findById(organizerDto.getRoleId()).orElseThrow(() -> new CustomException("Role not found"));
            organizer.getRoles().clear();
            organizer.addRole(role);
        }

        if (organizerDto.getEnabled() != null && !organizerDto.getEnabled().isEmpty()) {
            organizer.setEnabled(Boolean.parseBoolean(organizerDto.getEnabled()));
        }
    }

    @Transactional
    public void removeById(Long id) throws CustomException {
        organizerRepository.findById(id).orElseThrow(() -> new CustomException("Organizer not found by given id"));
        organizerRepository.deleteById(id);
    }

    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        organizerRepository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    private void saveUserToken(Organizer organizer, String jwtToken) {
        var token = Token.builder()
            .organizer(organizer)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Organizer organizer) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(organizer.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, CustomException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userDetailsService.loadUserByUsername(userEmail);
            Organizer organizer = organizerRepository.findOrganizerByEmail(userEmail).orElseThrow(() -> new CustomException("Organizer not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(organizer);
                saveUserToken(organizer, accessToken);
                var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    public String resetPassword(ResetPasswordDto resetPasswordDto, Long expoId) throws CustomException {

        checkValidExpoId(expoId);
        Organizer organizer = null;

        if (resetPasswordDto.getEmail() == null || resetPasswordDto.getEmail().isEmpty()) {
            organizer = organizerRepository.findOrganizerByPhoneNumberAndExpoId(resetPasswordDto.getPhoneNumber(), expoId)
                .orElseThrow(() -> new CustomException("Phone number not found in the expo"));
        } else {
            organizer = organizerRepository.findOrganizerByEmailAndExpoId(resetPasswordDto.getEmail(), expoId)
                .orElseThrow(() -> new CustomException("Email address not found in the expo"));
        }

        if (resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            organizer.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            return "Password reset successfully";
        }

        return "Confirmation password is wrong";
    }
}
