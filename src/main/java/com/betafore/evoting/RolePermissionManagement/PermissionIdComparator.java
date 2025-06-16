package com.betafore.evoting.RolePermissionManagement;

import java.util.Comparator;

public class PermissionIdComparator implements Comparator<Permission> {
    @Override
    public int compare(Permission o1, Permission o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
