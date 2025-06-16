package com.betafore.evoting.Util;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtil {

    public static String getCurrentUserPrincipal(){

        return SecurityContextHolder.getContext()
            .getAuthentication().getName();
    }
}
