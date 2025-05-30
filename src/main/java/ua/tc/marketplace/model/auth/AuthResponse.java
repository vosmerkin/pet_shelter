package ua.tc.marketplace.model.auth;


public record AuthResponse(
    Long id,
        String email,
     String accessToken,
     String refreshToken, // Optional, if using refresh tokens
    Long expiresInSeconds){ // Optional, seconds until expiration

}