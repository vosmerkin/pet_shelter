package ua.tc.marketplace.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tc.marketplace.exception.article.ArticleNotFoundException;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.entity.Article;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.model.enums.ArticleStatus;
import ua.tc.marketplace.model.enums.UserRole;
import ua.tc.marketplace.repository.ArticleRepository;
import ua.tc.marketplace.util.mapper.ArticleMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {
    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    private Long articleId;
    private Article article;
    private ArticleDto articleDto;
    private CreateArticleDto newArticle;

    @BeforeEach
    void setUp() {
        articleId = 1L;
        var articleAuthor = User.builder()
                .id(1L)
                .email("taras@shevchenko.ua")
                .password("password")
                .userRole(UserRole.USER)
                .firstName("Taras")
                .build();
        var articleCategory = Category.builder()
                .id(1L)
                .name("Category name")
                .build();
        var articleCategoryDto =
                new CategoryDto(1L,
                        "Category name",
                        Collections.emptyList());
        article = Article.builder()
                .id(articleId)
                .title("Title of the article")
                .content("Main content of the article")
                .author(articleAuthor)
                .category(articleCategory)
                .slug("article_slug")
                .build();
        newArticle = CreateArticleDto.builder()
                .title("Title of the article")
                .content("Main content of the article")
                .authorId(articleAuthor.getId())
                .categoryId(articleCategory.getId())
                .slug("article_slug")
                .build();
        articleDto = ArticleDto.builder()
                .id(articleId)
                .title("Title of the article")
                .content("Main content of the article")
                .authorId(articleAuthor.getId())
                .category(articleCategoryDto)
                .slug("article_slug")
                .status(ArticleStatus.DRAFT)
                .build();
    }

    @Test
    void findById_shouldReturnDto_whenExists() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // Mock mapper
        when(articleMapper.toDto(article)).thenReturn(articleDto);

        // run method
        ArticleDto result = articleService.findById(articleId);

        // Assert results
        assertEquals(articleDto, result);

        // Verify methods were called
        verify(articleRepository, times(1)).findById(articleId);
        verify(articleMapper, times(1)).toDto(article);
    }

    @Test
    void findById_shouldThrow_whenNotExists() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act and Assert
        // Use assertThrows to verify that TagNotFoundException is thrown
        assertThrows(ArticleNotFoundException.class, () -> articleService.findById(articleId));

        // Verify method calls
        verify(articleRepository, times(1)).findById(articleId);
        verify(articleMapper, times(0)).toDto(article);
    }

    @Test
    void createTag_shouldCreate_whenValidInput() {
        // Mock repository
        when(articleRepository.findBySlug(newArticle.slug())).thenReturn(Optional.empty());

        // Mock mapper
        when(articleMapper.toEntity(newArticle)).thenReturn(article);

        // Mock repository
        when(articleRepository.save(article)).thenReturn(article);

        // Mock mapper from entity back to Dto
        when(articleMapper.toDto(article)).thenReturn(articleDto);

        // Act
        ArticleDto result = articleService.create(newArticle);

        // Assert
        assertEquals(articleDto, result);

        // Verify method calls
        verify(articleRepository, times(1)).findBySlug(newArticle.slug());
        verify(articleMapper, times(1)).toEntity(newArticle);
        verify(articleRepository, times(1)).save(article);
        verify(articleMapper, times(1)).toDto(article);
    }

//    @Test
//    void createTag_shouldReturnExistingTag_whenTagNameExists() {
//        // Mock repository
//        when(articleRepository.findBySlug(newArticle.slug())).thenReturn(Optional.empty());
//
//
//
//    }

}
