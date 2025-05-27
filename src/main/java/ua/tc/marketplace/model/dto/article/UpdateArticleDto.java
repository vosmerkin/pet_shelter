package ua.tc.marketplace.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.tc.marketplace.model.enums.ArticleStatus;

import java.util.List;

/**
 * Data Transfer Object (DTO) for updating an article.
 */
@Builder
public record UpdateArticleDto(
        @NotEmpty @Schema(example = "How to Help Stray Animals") String title,
        @NotEmpty @Schema(example = "Providing food and shelter for stray animals can make a big difference...") String content,
        @NotNull Long categoryId,
        List<Long> tagIds,
        List<Long> photoIds,
        @Schema(example = "Ways to support and care for stray animals in your community") String metaDescription,
        String structuredData,
        @NotNull ArticleStatus status,
        @Schema(example = "false") Boolean isFeatured,
        @Schema(example = "how-to-adopt-a-rescue-dog") String slug
) {
}
