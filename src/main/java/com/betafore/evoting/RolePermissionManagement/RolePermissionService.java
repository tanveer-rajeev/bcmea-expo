package com.betafore.evoting.RolePermissionManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.Expo;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.ExpoServiceManagement.ExpoService;
import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.OrganizerManagement.OrganizerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ExpoRepository expoRepository;
    private final OrganizerRepository organizerRepository;

    public Set<Permission> getAllPermissionEnumByExpoId(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Expo expo = expoRepository.findById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id"));
        Set<ExpoService> expoServiceSet = expo.getExpoServiceSet();
        Stream<String[]> stream = expoServiceSet.stream().map(expoService -> expoService.getName().split(" "));
        List<String> serviceNameFirstPart = stream.map(strings -> strings[0].toLowerCase()).toList();

        return serviceNameFirstPart.stream().map(s -> {
                Set<Permission> res = new HashSet<>();

                for (Permission permission : getAllPermission()) {
                    if (permission.getName().startsWith(s)
                        || permission.getName().startsWith("organizer")
                        || permission.getName().startsWith("settings")
                        || permission.getName().startsWith("role")
                        || (s.equalsIgnoreCase("lottery") && permission.getName().startsWith("prize")))
                            res.add(permission);
                }
                return res;
            })
            .flatMap(Collection::stream)
            .sorted(new PermissionIdComparator())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    @Transactional
    public Role createRole(RoleDto roleDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Role role = Role.builder().expoId(expoId).name(roleDto.getRole().toUpperCase()).build();
        for (Long id : roleDto.getPermissionIds()) {
            Permission permission = permissionRepository.findById(id).orElseThrow(() -> new CustomException("Permission not found"));
            role.addPermission(permission);
        }
        roleRepository.save(role);
        return role;
    }

    public Role getRoleById(Long id) throws CustomException {
        return roleRepository.findById(id).orElseThrow(() -> new CustomException("Role not found by given id"));
    }

    public List<Role> getAllRoleByExpoId(Long expoId) throws CustomException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String principal = (String) authentication.getPrincipal();
        Organizer organizer = organizerRepository.findOrganizerByEmail(principal)
            .orElseThrow(() -> new CustomException("Organizer not found in the expo"));

        Role loggedInOrgRole = organizer.getRoles().stream().findFirst().get();
        List<Role> allByExpoId = roleRepository.findAllByExpoId(expoId);
        return allByExpoId.stream().filter(role -> role.getPermissions().size() <= loggedInOrgRole.getPermissions().size()).collect(Collectors.toList());
    }

    @Transactional
    public void updateRole(RoleDto roleDto, Long id) throws CustomException {
        Role role = getRoleById(id);
        if (roleDto.getRole() != null && !roleDto.getRole().isEmpty() && !roleDto.getRole().equals(role.getName())) {
            role.setName(roleDto.getRole());
        }

        if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
            role.getPermissions().clear();
            for (Long permissionId : roleDto.getPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new CustomException("Permission not found by given id"));
                role.addPermission(permission);

            }
        }
    }

    @Transactional
    public void deleteRoleById(Long id) throws CustomException {
        Role role = getRoleById(id);
        if (role.getOrganizers().isEmpty()) {
            roleRepository.deleteById(id);
            return;
        }
        throw new CustomException("This role is already assigned to the organizer. Please delete the organizer first.");
    }

    public Set<Organizer> getAllOrganizerByRole(Long id) throws CustomException {
        Role role = getRoleById(id);
        return role.getOrganizers();
    }

    public List<Permission> getAllPermissionsByRoleId(Long id) throws CustomException {
        Role role = getRoleById(id);
        Set<Permission> permissions = role.getPermissions();
        List<Permission> collect = new ArrayList<>(permissions.stream().toList());
        collect.sort(new PermissionIdComparator());

        return collect;
    }

    @Transactional
    public void deleteAllRoleByExpoId(Long id) {
        roleRepository.deleteAllByExpoId(id);
    }


    public List<String> getAllPermissionGroup() {
        List<Set<PermissionEnum>> collect = Stream.of(PermissionGroup.values())
            .map(PermissionGroup::getPermissions).toList();
        return collect.stream()
            .flatMap(permissionEnums -> permissionEnums.stream()
                .map(PermissionEnum::getPermission)
            ).toList();
    }

    @Transactional
    public void saveAllPermission() {
        for (String permissionEnum : getAllPermissionGroup()) {
            Optional<Permission> optionalPermission = permissionRepository.findByName(permissionEnum);
            if (optionalPermission.isPresent()) {
                continue;
            }
            Permission permission = Permission.builder().name(permissionEnum).build();
            permissionRepository.save(permission);
        }
    }

    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }


}
