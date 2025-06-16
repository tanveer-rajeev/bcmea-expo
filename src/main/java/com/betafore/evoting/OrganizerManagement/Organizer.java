package com.betafore.evoting.OrganizerManagement;

import com.betafore.evoting.ParticipantManagement.Participant;
import com.betafore.evoting.RolePermissionManagement.Role;
import com.betafore.evoting.entities.BaseEntity;
//import com.betafore.evoting.security_config.RoleEnum;
import com.betafore.evoting.security_config.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organizer")
public class Organizer extends BaseEntity {

    @NotBlank(message = "email is mandatory")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "phone number is mandatory")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    private boolean enabled;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "organizers_roles",
        joinColumns = @JoinColumn(
            name = "organizer_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();

    @Column(name = "expo_id")
    private Long expoId;

    @JsonIgnore
    @OneToMany(mappedBy = "organizer", orphanRemoval = true)
    private List<Token> tokens;

    public void addRole(Role role){
        if(roles == null){
            roles = new HashSet<>();
        }
        roles.add(role);
    }

}
