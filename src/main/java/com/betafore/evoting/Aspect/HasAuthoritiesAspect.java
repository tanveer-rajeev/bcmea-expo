package com.betafore.evoting.Aspect;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@Aspect
@Component
@Slf4j
public class HasAuthoritiesAspect {
    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(authorities)")
    public void hasAuthorities(final JoinPoint joinPoint, final CustomHasAuthority authorities) throws CustomException {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (!Objects.isNull(securityContext)) {
            final Authentication authentication = securityContext.getAuthentication();
            if (!Objects.isNull(authentication)) {
                String permission = authorities.authorities().getPermission();
                final String username = authentication.getName();
                final Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

                if (userAuthorities.stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permission))) {
                    log.error("User {} does not have the correct authorities required by endpoint", username);
                    throw new CustomException(UNAUTHORIZED);
                }

            } else {
                log.error("The authentication is null when checking endpoint access for user request");
                throw new CustomException(UNAUTHENTICATED);
            }
        } else {
            log.error("The security context is null when checking endpoint access for user request");
            throw new CustomException(UNAUTHENTICATED);
        }
    }
}
