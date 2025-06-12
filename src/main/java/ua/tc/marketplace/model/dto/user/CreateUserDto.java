package ua.tc.marketplace.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import ua.tc.marketplace.model.entity.ContactInfo;

/**
 * Data Transfer Object (DTO) specifically for creating new users.
 * Contains user information required for creating a new user account.
 *
 * <p>Validation constraints are applied to ensure data integrity and consistency.
 */
public record CreateUserDto(
    @Schema(example = "taras@shevchenko.ua")
    @NotBlank
    @Email
    String email,

    @Schema(example = "strong_secure_password_with_bigAndSmallLetters_and_digits_and_symbols")
    @NotBlank
    @Size(min = 8, max = 24)
    String password,

    @Schema(example = "USER")
    @NotBlank
    String userRole,

    @Schema(example = "Taras")
    @NotBlank
    String firstName,

    @Schema(example = "Shevchenko")
    String lastName, // optional

    MultipartFile profilePicture,

    ContactInfo contactInfo
) {

    @Override
    public String toString() {
        String hiddenPassword = "*".repeat(password.length());
        return "CreateUserDto{" +
            "contactInfo=" + contactInfo +
            ", email='" + email + '\'' +
            ", password='" + hiddenPassword + '\'' +
            ", userRole='" + userRole + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}

