package com.betafore.evoting.RolePermissionManagement;

import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions",fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public Permission(String name){
        this.name = name;
    }
}
