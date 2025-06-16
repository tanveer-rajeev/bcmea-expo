package com.betafore.evoting.RolePermissionManagement;

import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    private Long expoId;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Organizer> organizers = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_permissions",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "permissions_id", referencedColumnName = "id"))
    private Set<Permission> permissions;

    public void addAllPermission(Set<Permission> permission) {
        if (this.permissions == null || this.permissions.isEmpty()){
            this.permissions = new HashSet<>();
        }
        this.permissions.addAll(permission);
    }

    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        if (!this.permissions.isEmpty()) {
            this.permissions.remove(permission);
        }
    }

    public Role(String name) {
        this.name = name;
    }
}

