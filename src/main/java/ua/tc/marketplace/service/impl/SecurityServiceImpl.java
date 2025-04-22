package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.exception.auth.GeneralAuthenticationException;
import ua.tc.marketplace.facade.AdFacade;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.service.AuthenticationService;
import ua.tc.marketplace.service.SecurityService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service("securityService")
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private final AdFacade adFacade;

    public boolean hasAnyRoleAndAdOwnership(Long adId) {

        // Default to checking if user has ANY role
        return hasAnyRoleAndOwnership(adFacade.findAdById(adId).authorId(), new String[0]);
    }


    @Override
    public boolean hasAnyRoleAndOwnership(Long authorId) {
        // Default to checking if user has ANY role
        return hasAnyRoleAndOwnership(authorId, new String[0]);
    }

    @Override
    public boolean hasAnyRoleAndOwnership(Long authorId, String... requiredRoles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new UnauthenticatedException();
            log.info("User with id {} is not authenticated or no authentication info found", authorId);
            return false;
        }

//        log.debug("Authorities: {}",authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(", ")));

        // 2. Check roles (if any required)
        if (requiredRoles.length > 0) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean hasRequiredRole = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> Arrays.asList(requiredRoles).contains(authority));

            if (!hasRequiredRole) {
                log.info("User with id {} is not authorized to the method",  authorId);
                return false;
//                throw new UnauthorizedException(authorities.stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.joining(", ")));
            }
        }

        // 3. Check ownership
        User authenticatedUser = authenticationService.getAuthenticatedUser()
                .orElseThrow(() -> new GeneralAuthenticationException("Not authenticated."));

        if (Objects.equals(authorId, authenticatedUser.getId())){
            return true;
        } else {
            log.info("User with id {} is not the owner of the resource",  authorId);
            return false;
//            throw new OwnershipException(authenticatedUser.getEmail());
        }
    }
}