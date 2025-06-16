package com.betafore.evoting.RolePermissionManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private String role;
    private Set<Long> permissionIds;
}
