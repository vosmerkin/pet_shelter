package ua.tc.marketplace.model.mapper;

import org.mapstruct.*;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;
import ua.tc.marketplace.model.entity.Article;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.Tag;
import ua.tc.marketplace.model.entity.Photo;
import ua.tc.marketplace.util.mapper.CategoryMapper;
import ua.tc.marketplace.util.mapper.TagMapper;
import ua.tc.marketplace.util.mapper.UserMapper;

import java.util.List;

/**
 * Mapper interface using MapStruct for converting between Article entities and DTOs.
 * Defines mappings for converting Article to ArticleDto, CreateArticleDto, and UpdateArticleDto.
 */
@Mapper(config = MapperConfig.class, uses = {UserMapper.class, CategoryMapper.class, TagMapper.class})
public interface ArticleMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "tags", target = "tagIds", qualifiedByName = "mapTagsToIds")
    @Mapping(source = "photos", target = "photoIds", qualifiedByName = "mapPhotosToIds")
    ArticleDto toDto(Article entity);

    @Mapping(source = "authorId", target = "author")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "mapIdsToTags")
    @Mapping(source = "photoIds", target = "photos", qualifiedByName = "mapIdsToPhotos")
    Article toEntity(CreateArticleDto dto);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "mapIdsToTags")
    @Mapping(source = "photoIds", target = "photos", qualifiedByName = "mapIdsToPhotos")
    void updateEntityFromDto(@MappingTarget Article article, UpdateArticleDto dto);

    @Named("mapTagsToIds")
    static List<Long> mapTagsToIds(List<Tag> tags) {
        return tags != null ? tags.stream().map(Tag::getId).toList() : null;
    }

    @Named("mapPhotosToIds")
    static List<Long> mapPhotosToIds(List<Photo> photos) {
        return photos != null ? photos.stream().map(Photo::getId).toList() : null;
    }

    @Named("mapIdsToTags")
    static List<Tag> mapIdsToTags(List<Long> tagIds) {
        return tagIds != null ? tagIds.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).toList() : null;
    }

    @Named("mapIdsToPhotos")
    static List<Photo> mapIdsToPhotos(List<Long> photoIds) {
        return photoIds != null ? photoIds.stream().map(id -> {
            Photo photo = new Photo();
            photo.setId(id);
            return photo;
        }).toList() : null;
    }
}
