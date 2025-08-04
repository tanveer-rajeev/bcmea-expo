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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("OrganizerService:findByEmail Finding organizer by email: {}", email);
        Organizer organizer = organizerRepository.findOrganizerByEmail(email)
            .orElseThrow(() -> new CustomException("Organizer not found by given mail"));
        return entityToDto(organizer);
    }

    @Transactional
    public void createSuperAdmin(OrganizerDto request, String role) {
        Optional<Organizer> optionalOrganizer = organizerRepository.findOrganizerByEmail(request.getEmail());
        if (optionalOrganizer.isPresent()) {
            log.warn("OrganizerService:createSuperAdmin Attempted to create super admin but email already exists: {}", request.getEmail());
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
        log.info("OrganizerService:save saving organizer by expoId {}", expoId);

        checkValidExpoId(expoId);
        Optional<Organizer> optionalOrganizer = organizerRepository.findByEmailAndExpoId(request.getEmail(), expoId);
        if (optionalOrganizer.isPresent()) {
            log.warn("OrganizerService:save email already taken");
            throw new CustomException("Email already taken");
        }

        Optional<Organizer> organizerByPhoneNumberAndExpoId = organizerRepository.findOrganizerByPhoneNumberAndExpoId(request.getPhoneNumber(), expoId);
        if (organizerByPhoneNumberAndExpoId.isPresent()) {
            log.warn("OrganizerService:save phone number already taken");
            throw new CustomException("Phone number already taken");
        }
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() ->
            {
                log.warn("OrganizerService:save role not found");
                return new CustomException("Role not found");
            }
        );

        Organizer organizer = Organizer.builder()
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(Boolean.parseBoolean(request.getEnabled()))
            .expoId(expoId)
            .build();
        organizer.addRole(role);
        Organizer saved = organizerRepository.save(organizer);

        log.info("OrganizerService:save saved organizer");

        return entityToDto(saved);
    }

    private OrganizerDto entityToDto(Organizer organizer) {
        log.trace("OrganizerService:entityToDto mapping Organizer entity to DTO for id: {}", organizer.getId());

        Role role = organizer.getRoles().stream().findFirst().get();
        OrganizerDto organizerDto = OrganizerDto.builder()
            .id(organizer.getId())
            .phoneNumber(organizer.getPhoneNumber())
            .email(organizer.getEmail())
            .password(organizer.getPassword())
            .enabled(String.valueOf(organizer.isEnabled()))
            .expoId(organizer.getExpoId())
            .roleId(role.getId())
            .build();

        log.trace("OrganizerService:entityToDto mapped DTO: {}", organizerDto);

        return organizerDto;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, Long expoId) throws CustomException {
        log.info("OrganizerService:authenticate authenticating user with email: {}, expoId: {}", request.getEmail(), expoId);

        Optional<Organizer> byExpoId = organizerRepository.findByEmailAndExpoId(request.getEmail(), expoId);

        if (byExpoId.isEmpty()) {
            log.warn("OrganizerService:authenticate Authentication failed. Organizer not found for email: {}", request.getEmail());
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

        log.info("OrganizerService:authenticate authentication successful for: {}", request.getEmail());

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    public AuthenticationResponse authenticateByPhoneNumber(String phoneNumber, Long expoId) throws CustomException {
        log.info("OrganizerService:authenticateByPhoneNumber Authenticating user with phone number: {}, expoId: {}", phoneNumber, expoId);

        Optional<Organizer> organizerByPhoneNumberAndExpoId = organizerRepository
            .findOrganizerByPhoneNumberAndExpoId(phoneNumber, expoId);

        if (organizerByPhoneNumberAndExpoId.isEmpty()) {
            log.warn("OrganizerService:authenticateByPhoneNumber Authentication failed. Organizer not found with this phone number: {}", phoneNumber);
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

        log.info("OrganizerService:authenticateByPhoneNumber authentication successful for: {}", phoneNumber);

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    public OrganizerDto findById(Long id) throws CustomException {
        log.info("OrganizerService:findById fetching organizer by id: {}", id);

        Organizer organizer = organizerRepository.findById(id).orElseThrow(() -> {
            log.warn("OrganizerService:findById organizer not found with id: {}", id);
            return new CustomException("User not found: " + id);
        });

        log.debug("OrganizerService:findById organizer found with email: {}, enabled: {}",
            organizer.getEmail(), organizer.isEnabled());
        return entityToDto(organizer);
    }

    public List<OrganizerDto> all(Long expoId) throws CustomException {
        log.info("Fetching all organizer by id: {}", expoId);

        checkValidExpoId(expoId);

        List<Organizer> allByExpoId = organizerRepository.findAllByExpoId(expoId);

        log.debug("Found {} organizers for expoId: {}", allByExpoId.size(), expoId);

        return allByExpoId.stream().map(this::entityToDto)
            .collect(Collectors.toList());
    }

    public Role getAllRoleByOrganizerId(Long id) throws CustomException {
        log.info("Fetching roles for organizer id: {}", id);

        Organizer organizer = organizerRepository.findById(id).orElseThrow(() ->
        {
            log.warn("OrganizerService:getAllRoleByOrganizerId organizer not found with id: {}", id);
            return new CustomException("Organizer not found");
        });

        Collection<Role> roles = organizer.getRoles();

        if (roles.isEmpty()) {
            log.warn("OrganizerService:getAllRoleByOrganizerId no roles assigned for organizer id: {}", id);
            return null;
        }

        Role role = roles.stream().findFirst().get();

        log.debug("OrganizerService:getAllRoleByOrganizerId role found: {} for organizer id: {}", role.getName(), id);

        return role;
    }

    @Transactional
    public void update(Long id, OrganizerDto organizerDto) throws CustomException {
        log.info("OrganizerService:update updating organizer with id: {}", id);

        Optional<Organizer> optionalOrganizer = organizerRepository.findById(id);

        if (optionalOrganizer.isEmpty()) {
            log.warn("OrganizerService:update organizer not found with id: {}", id);
            throw new CustomException("User not found by given id");
        }

        Organizer organizer = optionalOrganizer.get();

        if (organizerDto.getEmail() != null && !organizerDto.getEmail().isEmpty()
            && !Objects.equals(organizer.getEmail(), organizerDto.getEmail())) {
            Optional<Organizer> byEmail = organizerRepository.findOrganizerByEmail(organizerDto.getEmail());

            if (byEmail.isPresent()) {
                log.warn("OrganizerService:update email already taken: {}", organizerDto.getEmail());
                throw new CustomException("Email already taken. Please try different");
            }

            organizer.setEmail(organizerDto.getEmail());
            log.debug("OrganizerService:update organizer email updated to: {}", organizerDto.getEmail());
        }

        if (organizerDto.getPhoneNumber() != null && !organizerDto.getPhoneNumber().isEmpty()
            && !Objects.equals(organizer.getPhoneNumber(), organizerDto.getPhoneNumber())) {

            Optional<Organizer> byPhone = organizerRepository.findByPhoneNumber(organizerDto.getPhoneNumber());

            if (byPhone.isPresent()) {
                log.warn("OrganizerService:update phone number already taken: {}", organizerDto.getPhoneNumber());
                throw new CustomException("Phone number already taken. Please try different");
            }

            organizer.setPhoneNumber(organizerDto.getPhoneNumber());
            log.debug("OrganizerService:update organizer phone number updated to: {}", organizerDto.getPhoneNumber());
        }

        if (organizerDto.getPassword() != null && !organizerDto.getPassword().isEmpty()) {
            organizer.setPassword(passwordEncoder.encode(organizerDto.getPassword()));
            log.debug("OrganizerService:update organizer password updated");
        }

        if (organizerDto.getRoleId() != null
            && !organizer.getRoles().stream().findFirst().get().getId().equals(organizerDto.getRoleId())) {

            Role role = roleRepository.findById(organizerDto.getRoleId())
                .orElseThrow(() -> {
                    log.warn("OrganizerService:update role not found");
                    return new CustomException("Role not found");
                });

            organizer.getRoles().clear();
            organizer.addRole(role);

            log.debug("OrganizerService:update organizer role updated to roleId: {}", organizerDto.getRoleId());
        }

        if (organizerDto.getEnabled() != null && !organizerDto.getEnabled().isEmpty()) {
            organizer.setEnabled(Boolean.parseBoolean(organizerDto.getEnabled()));
            log.debug("OrganizerService:update organizer enabled status updated to: {}", organizerDto.getEnabled());
        }
    }

    @Transactional
    public void removeById(Long id) throws CustomException {
        log.info("OrganizerService:removeById removing organizer with id: {}", id);

        organizerRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("OrganizerService:removeById organizer not found with id: {}", id);
                return new CustomException("Organizer not found by given id");
            });

        organizerRepository.deleteById(id);

        log.info("OrganizerService:removeById organizer removed with id: {}", id);
    }

    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        log.info("OrganizerService:removeAll Removing all organizers for expoId: {}", expoId);

        checkValidExpoId(expoId);

        organizerRepository.deleteAllByExpoId(expoId);

        log.info("OrganizerService:removeAll all organizers removed for expoId: {}", expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        log.trace("OrganizerService:checkValidExpoId Validating expoId: {}", expoId);

        expoRepository.findEnableExpoById(expoId)
            .orElseThrow(() -> {
                log.warn("OrganizerService:checkValidExpoId expo not found with id: {}", expoId);
                return new CustomException("Expo not found by given id: " + expoId);
            });
    }

    private void saveUserToken(Organizer organizer, String jwtToken) {
        log.debug("OrganizerService:checkValidExpoId saving token for organizer id: {}", organizer.getId());

        var token = Token.builder()
            .organizer(organizer)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);

        log.debug("OrganizerService:checkValidExpoId token saved for organizer id: {}", organizer.getId());
    }

    private void revokeAllUserTokens(Organizer organizer) {
        log.debug("OrganizerService:checkValidExpoId revoking all tokens for organizer id: {}", organizer.getId());

        var validUserTokens = tokenRepository.findAllValidTokenByUser(organizer.getId());

        if (validUserTokens.isEmpty()) {
            log.debug("No valid tokens to revoke for organizer id: {}", organizer.getId());
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);

        log.debug("OrganizerService:checkValidExpoId revoked all tokens for organizer id: {}", organizer.getId());
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, CustomException {
        log.info("OrganizerService:refreshToken refreshing jwt token");

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("OrganizerService:refreshToken missing Bearer into auth header");
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            log.debug("OrganizerService:refreshToken getting user details from user context");

            var user = userDetailsService.loadUserByUsername(userEmail);
            Organizer organizer = organizerRepository.findOrganizerByEmail(userEmail).orElseThrow(() -> new CustomException("Organizer not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                log.debug("OrganizerService:refreshToken token is valid");
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
        log.info("OrganizerService:resetPassword resetting password");

        checkValidExpoId(expoId);

        Organizer organizer = null;

        if (resetPasswordDto.getEmail() == null || resetPasswordDto.getEmail().isEmpty()) {
            organizer = organizerRepository.findOrganizerByPhoneNumberAndExpoId(resetPasswordDto.getPhoneNumber(), expoId)
                .orElseThrow(() -> {
                    log.error("OrganizerService:resetPassword phone number not found");
                    return new CustomException("Phone number not found in the expo");
                });
        } else {
            organizer = organizerRepository.findOrganizerByEmailAndExpoId(resetPasswordDto.getEmail(), expoId)
                .orElseThrow(() -> {
                    log.error("OrganizerService:resetPassword email not found");
                    return new CustomException("Email address not found in the expo");
                });
        }

        if (resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            log.debug("OrganizerService:resetPassword password resetting");
            organizer.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            return "Password reset successfully";
        }

        log.info("OrganizerService:resetPassword password reset successfully");

        return "Confirmation password is wrong";
    }
}
