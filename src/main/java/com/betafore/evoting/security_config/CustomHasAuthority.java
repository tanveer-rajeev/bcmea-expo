package com.betafore.evoting.security_config;

import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface CustomHasAuthority {

    PermissionEnum authorities();
}
