package ua.tc.marketplace.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tc.marketplace.exception.article.ArticleNotFoundException;
import ua.tc.marketplace.exception.article.ArticleSlugInUseException;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;
import ua.tc.marketplace.model.entity.Article;
import ua.tc.marketplace.repository.ArticleRepository;
import ua.tc.marketplace.repository.CategoryRepository;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.service.ArticleService;
import ua.tc.marketplace.util.mapper.ArticleMapper;

import java.util.Optional;

/**
 * Implementation of the {@link ArticleService} interface. Provides methods for creating, retrieving,
 * updating, and deleting articles related to pet adoption and shelter topics.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Retrieves a paginated list of all articles.
     *
     * @param pageable Pagination information (page number, size, sorting).
     * @return A page of ArticleDto objects.
     */
    @Override
    public Page<ArticleDto> findAll(Pageable pageable) {
        Page<Article> articles = articleRepository.findAll(pageable);
        return articles.map(articleMapper::toDto);
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param id The ID of the article to retrieve.
     * @return The ArticleDto representing the article.
     * @throws ArticleNotFoundException If the Article is not found.
     */
    @Override
    public ArticleDto findById(Long id) {
        Article article = getArticle(id);
        return articleMapper.toDto(article);
    }

    /**
     * Creates a new article.
     *
     * @param createArticleDto The DTO containing article information for creation.
     * @return The created ArticleDto.
     */
    @Transactional
    @Override
    public ArticleDto create(CreateArticleDto createArticleDto) {
        Optional<Article> existingArticle = articleRepository.findBySlug(createArticleDto.slug());
        if (existingArticle.isPresent()) {
            throw new ArticleSlugInUseException(createArticleDto.slug());
        }
        Article article = articleMapper.toEntity(createArticleDto);

        // Set author and category
        article.setAuthor(userRepository.findById(createArticleDto.authorId()).orElseThrow());
        article.setCategory(categoryRepository.findById(createArticleDto.categoryId()).orElseThrow());

        return articleMapper.toDto(articleRepository.save(article));
    }

    /**
     * Updates an existing article.
     *
     * @param id               The id of the article to update.
     * @param updateArticleDto The DTO containing updated article information.
     * @return The updated ArticleDto.
     * @throws ArticleNotFoundException If the article to update is not found.
     */
    @Transactional
    @Override
    public ArticleDto update(Long id, @NonNull UpdateArticleDto updateArticleDto) {
        Article existingArticle = getArticle(id);
        Optional<Article> sameSlugArticle = articleRepository.findBySlug(updateArticleDto.slug());
        if (sameSlugArticle.isPresent()) throw new ArticleSlugInUseException(updateArticleDto.slug());
        articleMapper.updateEntityFromDto(existingArticle, updateArticleDto);
        return articleMapper.toDto(articleRepository.save(existingArticle));
    }

    /**
     * Deletes an article by its ID.
     *
     * @param id The ID of the article to delete.
     * @throws ArticleNotFoundException If the article to delete is not found.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting article with id={}", id);
        Article existingArticle = getArticle(id);
        articleRepository.deleteById(existingArticle.getId());
    }

    /**
     * Retrieves an article by its ID, throwing an ArticleNotFoundException if not found.
     *
     * @param id The ID of the article to retrieve.
     * @return The found Article entity.
     * @throws ArticleNotFoundException If the article is not found.
     */
    private Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
    }
}
