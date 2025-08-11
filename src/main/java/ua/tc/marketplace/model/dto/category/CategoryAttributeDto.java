package ua.tc.marketplace.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * Data Transfer Object for a category attribute.
 * This class is used to transfer data between the client and the server.
 */

public record CategoryAttributeDto(
        Long id,
        @Schema(example = "cat") @NotNull Long categoryId,
        @Schema(example = "age") @NotNull Long attributeId,
        Set<String> values
)
{
}