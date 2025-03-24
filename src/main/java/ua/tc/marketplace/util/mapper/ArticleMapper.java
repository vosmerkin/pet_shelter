package ua.tc.marketplace.util.mapper;

import org.mapstruct.*;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;
import ua.tc.marketplace.model.entity.*;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.service.TagService;

import java.util.List;

/**
 * Mapper interface using MapStruct for converting between Article entities and DTOs.
 * Defines mappings for converting Article to ArticleDto, CreateArticleDto, and UpdateArticleDto.
 */
@Mapper(componentModel = "spring", config = MapperConfig.class, uses = {
        UserMapper.class,
        CategoryMapper.class,
        CategoryService.class,
        TagService.class,
        UserMapperResolver.class})
public interface ArticleMapper {

    @Mapping(source = "author.id", target = "authorId")
//    @Mapping(source = "author", target = "authorId", qualifiedByName = "mapAuthorToId")
//    @Mapping(source = "category", target = "categoryId")
//    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToIds")
//    @Mapping(source = "photos", target = "photos", qualifiedByName = "mapPhotosToIds")
    ArticleDto toDto(Article entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "authorId", target = "author", qualifiedByName = "mapIdToUser")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "idToTagList")
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "likes", ignore = true)
    Article toEntity(CreateArticleDto dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "idToTagList")
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "likes", ignore = true)
    void updateEntityFromDto(@MappingTarget Article article, UpdateArticleDto dto);

    @Named("idToTagList")
    List<Tag> idToTagList(List<Long> tagIds);
}
