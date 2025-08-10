package ua.tc.marketplace.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * Data Transfer Object for updating category attribute options.
 * This class is used to transfer data between the client and the server.
 */

public record UpdateCategoryAttributeDto(
        Set<String> values
)
{
}