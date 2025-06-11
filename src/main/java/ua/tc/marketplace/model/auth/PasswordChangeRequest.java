package ua.tc.marketplace.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a password change request containing the user's email, password reset token,
 * and the new password.
 * <p>
 * The email must be a valid email address and cannot be blank.
 * The password reset token must not be blank.
 * The new password must be between 8 and 24 characters long and cannot be blank.
 * </p>
 */
public record PasswordChangeRequest(
    @NotBlank @Email String email,
    @NotBlank String passwordResetToken,
    @NotBlank @Size(min = 8, max = 24) String password) {

  @Override
  public String toString() {
    String hiddenPassword = "*".repeat(password.length());
    return "AuthRequest{" +
        "email='" + email + '\'' +
        "resetToken='" + passwordResetToken + '\'' +
        ", password='" + hiddenPassword + '\'' +
        '}';
  }
}
