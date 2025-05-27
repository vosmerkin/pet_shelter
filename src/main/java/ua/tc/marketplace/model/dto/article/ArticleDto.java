package ua.tc.marketplace.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.dto.tag.TagDto;
import ua.tc.marketplace.model.entity.Photo;
import ua.tc.marketplace.model.enums.ArticleStatus;

/**
 * Data Transfer Object (DTO) representing an article.
 * Used for transferring article data between layers of the application.
 */
@Builder
public record ArticleDto(
        Long id,
        @NotEmpty @Schema(example = "How to Adopt a Rescue Dog") String title,
        @NotEmpty @Schema(example = "Adopting a rescue dog can be a rewarding experience...") String content,
        @NotNull(message = "Author ID cannot be null") Long authorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<Photo> photos,
        List<TagDto> tags,
        @NotNull CategoryDto category,
        @Schema(example = "A complete guide to adopting rescue dogs") String metaDescription,
        String structuredData,
        @NotNull ArticleStatus status,
        @Schema(example = "true") Boolean isFeatured,
        @Schema(example = "25") Integer likes,
        @Schema(example = "how-to-adopt-a-rescue-dog") String slug
) {}