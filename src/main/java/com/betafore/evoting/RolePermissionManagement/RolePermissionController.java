package com.betafore.evoting.RolePermissionManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_CREATE)
    @PostMapping("/role/{expoId}")
    public ResponseEntity<ApiResponse> createRole(@PathVariable Long expoId,@RequestBody RoleDto roleDto) throws CustomException {
        return ResponseEntity.status(201)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(rolePermissionService.createRole(roleDto,expoId))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_DELETE)
    @DeleteMapping("/role/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) throws CustomException {
        rolePermissionService.deleteRoleById(id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_DELETE)
    @DeleteMapping("/role/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllRoleByExpoId(@PathVariable Long expoId){
        rolePermissionService.deleteAllRoleByExpoId(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_READ)
    @GetMapping("/role/getAllOrganizer/{roleId}")
    public ResponseEntity<ApiResponse> getAllOrganizerByRole(@PathVariable Long roleId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(rolePermissionService.getAllOrganizerByRole(roleId))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_READ)
    @GetMapping("/role/{id}")
    public ResponseEntity<ApiResponse> getRole(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data( rolePermissionService.getRoleById(id))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_READ)
    @GetMapping("/role/all/permissions/{id}")
    public ResponseEntity<ApiResponse> getAllPermissionsByRoleId(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data( rolePermissionService.getAllPermissionsByRoleId(id))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_READ)
    @GetMapping("/role/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllRoleByExpoId(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data( rolePermissionService.getAllRoleByExpoId(expoId))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_UPDATE)
    @PutMapping("/role/{id}")
    public ResponseEntity<ApiResponse> updateRole(@RequestBody RoleDto roleDto,@PathVariable Long id) throws CustomException {
        rolePermissionService.updateRole(roleDto,id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/permissions/all")
    public ResponseEntity<ApiResponse> getAllPermission() {
        return ResponseEntity.ok().body(
            ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(rolePermissionService.getAllPermission())
                .build()
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/permissions/save/all")
    public ResponseEntity<ApiResponse> saveAllPermission() {
        rolePermissionService.saveAllPermission();
        return ResponseEntity.ok().body(
            ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build()
        );
    }

    @CustomHasAuthority(authorities = PermissionEnum.ROLE_READ)
    @GetMapping("/permissions/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllPermissionByExpoId(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok().body(
            ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(rolePermissionService.getAllPermissionEnumByExpoId(expoId))
                .build()
        );
    }
}
