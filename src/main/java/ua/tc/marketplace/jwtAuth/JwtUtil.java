package ua.tc.marketplace.jwtAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.service.impl.UserDetailsServiceImpl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final UserDetailsServiceImpl userDetailsService;


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime()
                + TimeUnit.SECONDS.toMillis(jwtConfig.getTokenExpirationAfterSeconds()));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(jwtConfig.getSecretKeyHmac(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKeyHmac()).build()
                .parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null) {
            return parseJwtClaims(token);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtConfig.getTokenPrefix())) {
            return bearerToken.substring(jwtConfig.getTokenPrefix().length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        return claims.getExpiration().after(new Date());

    }

    public boolean validateToken(String jwtToken) {
//        try { // Parse and verify the token
        parseJwtClaims(jwtToken);
        return true;
//        } catch (Exception e) {
//            throw new GeneralAuthenticationException("JWT token not valid"); // Token is invalid (expired, malformed, etc.)
//        }
    }

    public boolean isTokenExpired(String token) {
        if (token != null) {
            Claims claims = parseJwtClaims(token);
            if (claims != null) {
                return claims
                        .getExpiration()
                        .after(new Date());
            }
        }
        return false;
    }

    public String extractEmail(String jwtToken) {
        Claims claims = parseJwtClaims(jwtToken);
        if (claims != null && validateClaims(claims)) return claims.getSubject(); // Returns the "sub" (subject) claim
        return null;
    }

    public Authentication getAuthentication(String token) {
        String email = extractEmail(token);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        email, "",
                        userDetailsService.loadUserByUsername(email).getAuthorities());
        return authentication;
    }

}
