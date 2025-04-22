package ua.tc.marketplace.jwtAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.tc.marketplace.exception.auth.ExpiredTokenException;
import ua.tc.marketplace.exception.auth.InvalidTokenException;
import ua.tc.marketplace.exception.auth.MissingTokenException;
import ua.tc.marketplace.exception.model.ErrorResponse;
import ua.tc.marketplace.service.impl.UserDetailsServiceImpl;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null) {
                throw new MissingTokenException();
//                filterChain.doFilter(request, response);
//                return;
            }
            if (!jwtUtil.validateToken(accessToken)) {
                if (jwtUtil.isTokenExpired(accessToken)) {
                    throw new ExpiredTokenException();
                } else {
                    throw new InvalidTokenException();
                }
            }
            Authentication auth = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
//            Claims claims = jwtUtil.resolveClaims(request);
//            if (claims != null && jwtUtil.validateClaims(claims)) {
//                String email = claims.getSubject();
//                log.debug("Authorities - {}", userDetailsService.loadUserByUsername(email).getAuthorities().toString());
//                Authentication authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                email, "",
//                                userDetailsService.loadUserByUsername(email).getAuthorities());
//            }

        } catch (Exception e) {
            log.error("Authentication error - {}", e.getMessage());
            request.setAttribute("Authentication error",e);
//            ErrorResponse errorResponse = new ErrorResponse(
//                    HttpServletResponse.SC_UNAUTHORIZED,
//                    e.getMessage(),
//                    request.getRequestURI());
//            errorResponse.appendToResponse(response, mapper);
//            return;
        }
        filterChain.doFilter(request, response);
    }
}
