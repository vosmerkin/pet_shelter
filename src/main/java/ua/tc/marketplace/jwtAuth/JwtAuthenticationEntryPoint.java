package ua.tc.marketplace.jwtAuth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Retrieve the custom exception from the request attributes
//        Exception exception = (Exception) request.getAttribute("authException");

        // Default error response
        String errorMessage = "Authentication failed";
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;

//        if (exception instanceof MissingTokenException) {
//            errorMessage = "No token provided in the request";
//        } else if (exception instanceof ExpiredTokenException) {
//            errorMessage = "The token has expired";
//        } else if (exception instanceof InvalidTokenException) {
//            errorMessage = "The token is invalid";
//        }

        // Write the error response
        response.setStatus(statusCode);
        response.setContentType("application/json");

        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }
}
