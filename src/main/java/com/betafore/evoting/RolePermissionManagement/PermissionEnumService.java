package com.betafore.evoting.RolePermissionManagement;

import com.betafore.evoting.ExpoManagement.ExpoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PermissionEnumService {

    private final ExpoRepository expoRepository;

    public Set<String> getAllPermissionEnum() {
        return Stream.of(PermissionEnum.values())
            .map(PermissionEnum::getPermission)
            .collect(Collectors.toSet());
    }

    public Set<String> getASetOfPermissionEnumByPrefix(String prefix) {
        String lowerCase = prefix.toLowerCase();
        return Stream.of(PermissionEnum.values())
            .map(PermissionEnum::getPermission)
            .filter(permission -> permission.startsWith(lowerCase))
            .collect(Collectors.toSet());
    }


}
