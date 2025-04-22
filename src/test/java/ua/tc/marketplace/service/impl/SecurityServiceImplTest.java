package ua.tc.marketplace.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.tc.marketplace.exception.auth.GeneralAuthenticationException;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.service.AuthenticationService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testHasAnyRoleAndOwnership_Success() {
        // Mock Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock User
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        when(authenticationService.getAuthenticatedUser()).thenReturn(Optional.of(authenticatedUser));

        // Test
        boolean result = securityService.hasAnyRoleAndOwnership(1L, "USER");

        // Assertions
        assertTrue(result);
        verify(authenticationService, times(1)).getAuthenticatedUser();
    }

    @Test
    void testHasAnyRoleAndOwnership_NoAuthentication() {
        // No Authentication set
        SecurityContextHolder.clearContext();

        // Test
        boolean result = securityService.hasAnyRoleAndOwnership(1L, "ROLE_USER");

        // Assertions
        assertFalse(result);
    }

    @Test
    void testHasAnyRoleAndOwnership_Unauthorized() {
        // Mock Authentication with wrong role
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock User
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
//        when(authenticationService.getAuthenticatedUser()).thenReturn(Optional.of(authenticatedUser));

        // Test
        boolean result = securityService.hasAnyRoleAndOwnership(1L, "USER");

        // Assertions
        assertFalse(result);
        verify(authenticationService, times(0)).getAuthenticatedUser();
    }

    @Test
    void testHasAnyRoleAndOwnership_NotOwner() {
        // Mock Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock User with different ID
        User authenticatedUser = new User();
        authenticatedUser.setId(2L);
        when(authenticationService.getAuthenticatedUser()).thenReturn(Optional.of(authenticatedUser));

        // Test
        boolean result = securityService.hasAnyRoleAndOwnership(1L, "ROLE_USER");

        // Assertions
        assertFalse(result);
    }

    @Test
    void testHasAnyRoleAndOwnership_ExceptionHandling() {
        // Mock Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock AuthenticationService to throw an exception
        when(authenticationService.getAuthenticatedUser()).thenThrow(new GeneralAuthenticationException("Error"));

        // Test
        assertThrows(GeneralAuthenticationException.class, () -> securityService.hasAnyRoleAndOwnership(1L, "ROLE_USER"));
    }
}
