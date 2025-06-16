package com.betafore.evoting.security_config;

import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.OrganizerManagement.OrganizerRepository;
import com.betafore.evoting.RolePermissionManagement.Permission;
import com.betafore.evoting.RolePermissionManagement.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private OrganizerRepository organizerRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrPhoneNumber) throws UsernameNotFoundException {
        try {
            Optional<Organizer> optionalUser = organizerRepository.findByEmailOrPhoneNumber(emailOrPhoneNumber, emailOrPhoneNumber);
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("No organizer found by given email or phone number: " + emailOrPhoneNumber);
            }
            Organizer organizer = optionalUser.get();
            return new org.springframework.security.core.userdetails
                .User(organizer.getEmail(), organizer.getPassword(), organizer.isEnabled(),
                true, true, true,
                getAuthorities(organizer.getRoles()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPermissions(roles));
    }

    private List<String> getPermissions(final Collection<Role> roles) {
        final List<String> permissions = new ArrayList<>();
        final List<Permission> collection = new ArrayList<>();

        for (final Role role : roles) {
            permissions.add("ROLE_" + role.getName());
            collection.addAll(role.getPermissions());
        }
        for (final Permission item : collection) {
            permissions.add(item.getName());
        }

        return permissions;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> permissions) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : permissions) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
