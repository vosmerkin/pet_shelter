package ua.tc.marketplace.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tc.marketplace.exception.article.ArticleNotFoundException;
import ua.tc.marketplace.exception.article.ArticleSlugInUseException;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;
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
    private UpdateArticleDto updateArticleDto;
    private Article updatedArticle;
    private ArticleDto updatedArticleDto;
    private Article sameSlugArticle;

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
        sameSlugArticle = Article.builder()
                .id(articleId+1)
                .title("Title of the same_slug_article")
                .content("Main content of the same_slug_article")
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
        updateArticleDto = UpdateArticleDto.builder()
                .title("Changed Title")
                .content("Changed content of the article")
                .categoryId(articleCategoryDto.getId())
                .slug("article_slug")
                .status(ArticleStatus.DRAFT)
                .build();
        updatedArticle = Article.builder()
                .id(articleId)
                .title("Changed Title")
                .content("Changed content of the article")
                .author(articleAuthor)
                .category(articleCategory)
                .slug("article_slug")
                .build();
        updatedArticleDto = ArticleDto.builder()
                .id(articleId)
                .title("Changed Title")
                .content("Changed content of the article")
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
    void create_shouldCreate_whenValidInput() {
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

    @Test
    void create_shouldThrow_whenSlugInUse() {
        // Mock repository
        when(articleRepository.findBySlug(newArticle.slug())).thenReturn(Optional.of(article));

        // Act and Assert
        // Use assertThrows to verify that Exception is thrown
        assertThrows(ArticleSlugInUseException.class, () -> articleService.create(newArticle));

        // Verify method calls
        verify(articleRepository, times(1)).findBySlug(newArticle.slug());
        verify(articleMapper, times(0)).toEntity(any(CreateArticleDto.class));
        verify(articleRepository, times(0)).save(any(Article.class));
        verify(articleMapper, times(0)).toDto(any(Article.class));
    }

    @Test
    void update_shouldUpdate_whenValidInput_SameSlugArticleNotFound() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(articleRepository.findBySlug(updateArticleDto.slug())).thenReturn(Optional.empty());

        // Mock mapper to return updated entity when updateEntityFromDto is called
        doAnswer(
                invocation -> {
                    UpdateArticleDto dto = invocation.getArgument(1);
                    Article entityToUpdate = invocation.getArgument(0);
                    entityToUpdate.setSlug(dto.slug());
                    return null;
                })
                .when(articleMapper)
                .updateEntityFromDto(article, updateArticleDto);

        // Mock repository
        when(articleRepository.save(article)).thenReturn(updatedArticle);

        // Mock mapper
        when(articleMapper.toDto(updatedArticle)).thenReturn(updatedArticleDto);

        // Act
        ArticleDto result = articleService.update(articleId,updateArticleDto);

        // Assert
        assertEquals(updatedArticleDto, result);

        // Verify method calls
        verify(articleRepository,times(1)).findById(articleId);
        verify(articleRepository,times(1)).findBySlug(updateArticleDto.slug());
        verify(articleMapper,times(1)).updateEntityFromDto(article,updateArticleDto);
        verify(articleRepository,times(1)).save(article);
        verify(articleMapper,times(1)).toDto(updatedArticle);
    }

    @Test
    void update_shouldUpdate_whenValidInput_SameSlugArticleIsSameArticle() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(articleRepository.findBySlug(updateArticleDto.slug())).thenReturn(Optional.of(article));

        // Mock mapper to return updated entity when updateEntityFromDto is called
        doAnswer(
                invocation -> {
                    UpdateArticleDto dto = invocation.getArgument(1);
                    Article entityToUpdate = invocation.getArgument(0);
                    entityToUpdate.setSlug(dto.slug());
                    return null;
                })
                .when(articleMapper)
                .updateEntityFromDto(article, updateArticleDto);

        // Mock repository
        when(articleRepository.save(article)).thenReturn(updatedArticle);

        // Mock mapper
        when(articleMapper.toDto(updatedArticle)).thenReturn(updatedArticleDto);

        // Act
        ArticleDto result = articleService.update(articleId,updateArticleDto);

        // Assert
        assertEquals(updatedArticleDto, result);

        // Verify method calls
        verify(articleRepository,times(1)).findById(articleId);
        verify(articleRepository,times(1)).findBySlug(updateArticleDto.slug());
        verify(articleMapper,times(1)).updateEntityFromDto(article,updateArticleDto);
        verify(articleRepository,times(1)).save(article);
        verify(articleMapper,times(1)).toDto(updatedArticle);
    }

    @Test
    void update_shouldThrow_whenIdNotExists() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
//        when(articleRepository.findBySlug(updateArticleDto.slug())).thenReturn(Optional.empty());


        // Act and Assert
        // Use assertThrows to verify that Exception is thrown
        assertThrows(ArticleNotFoundException.class, () -> articleService.update(articleId,updateArticleDto));

        // Verify method calls
        verify(articleRepository,times(1)).findById(articleId);
        verify(articleRepository,times(0)).findBySlug(updateArticleDto.slug());
        verify(articleMapper,times(0)).updateEntityFromDto(any(Article.class),any(UpdateArticleDto.class));
        verify(articleRepository,times(0)).save(any(Article.class));
        verify(articleMapper,times(0)).toDto(any(Article.class));
    }

    @Test
    void update_shouldThrow_whenNewSlugIsAlreadyInUse() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(articleRepository.findBySlug(updateArticleDto.slug())).thenReturn(Optional.of(sameSlugArticle));

        // Act and Assert
        // Use assertThrows to verify that Exception is thrown
        assertThrows(ArticleSlugInUseException.class, () -> articleService.update(articleId,updateArticleDto));

        // Verify method calls
        verify(articleRepository,times(1)).findById(articleId);
        verify(articleRepository,times(1)).findBySlug(updateArticleDto.slug());
        verify(articleMapper,times(0)).updateEntityFromDto(any(Article.class),any(UpdateArticleDto.class));
        verify(articleRepository,times(0)).save(any(Article.class));
        verify(articleMapper,times(0)).toDto(any(Article.class));
    }

    @Test
    void delete_shouldDelete() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        //Act
        articleService.deleteById(articleId);

        // Verify method calls
        verify(articleRepository,times(1)).findById(articleId);
        verify(articleRepository,times(1)).deleteById(articleId);
    }


    @Test
    void delete_shouldDThrow_whenArticleIdNotFound() {
        // Mock repository
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act and Assert
        // Use assertThrows to verify that Exception is thrown
        assertThrows(ArticleNotFoundException.class, () -> articleService.deleteById(articleId));

        // Verify method calls
        verify(articleRepository,times(1)).findById(articleId);
        verify(articleRepository,times(0)).deleteById(anyLong());
    }

}
