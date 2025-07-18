package ua.tc.marketplace.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.ContactInfo;

/**
 * Data Transfer Object (DTO) for updating an existing user. Contains user information for updating
 * an existing user account.
 *
 * <p>Validation constraints are applied to ensure data integrity and consistency.
 */
@Builder
public record UpdateUserDto(
    Long id,
    @Schema(example = "taras@shevchenko.ua")
        //    @NotBlank
        String email,
    @Schema(example = "strong_secure_password_with_bigAndSmallLetters_and_digits_and_symbols")
        //    @NotBlank
        String password,
    @Schema(example = "USER")
        //    @NotBlank
        String userRole,
    @Schema(example = "Taras")
        //    @NotBlank
        String firstName,
    @Schema(example = "Shevchenko") String lastName, // optional
    ContactInfo contactInfo,
    List<Ad> favorites) {}
