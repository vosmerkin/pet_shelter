package ua.tc.marketplace.util.mapper;

import org.mapstruct.*;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;
import ua.tc.marketplace.model.entity.*;
import ua.tc.marketplace.service.CategoryService;

import java.util.List;

/**
 * Mapper interface using MapStruct for converting between Article entities and DTOs.
 * Defines mappings for converting Article to ArticleDto, CreateArticleDto, and UpdateArticleDto.
 */
@Mapper(config = MapperConfig.class, uses = {
        UserMapper.class,
        CategoryMapper.class,
        CategoryService.class,
        TagMapper.class,
        UserMapperResolver.class})
public interface ArticleMapper {

    @Mapping(source = "author", target = "authorId", qualifiedByName = "mapAuthorToId")
//    @Mapping(source = "category", target = "categoryId")
//    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToIds")
//    @Mapping(source = "photos", target = "photos", qualifiedByName = "mapPhotosToIds")
    ArticleDto toDto(Article entity);

    @Mapping(source = "authorId", target = "author", qualifiedByName = "mapIdToUser")
//    @Mapping(source = "categoryId", target = "category")
//    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "idToTagList")
    @Mapping(target = "photos", ignore = true)
    Article toEntity(CreateArticleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "category")
//    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapIdToCategory")
    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "idToTagList")
    @Mapping(target = "photos", ignore = true)
    void updateEntityFromDto(@MappingTarget Article article, UpdateArticleDto dto);

    @Named("mapAuthorToId")
    Long mapAuthorToId(User author);

//    @Named("mapPhotosToIds")
//    static List<Long> mapPhotosToIds(List<Photo> photos) {
//        return photos != null ? photos.stream().map(Photo::getId).toList() : null;
//    }
//    @Named("mapTagsToIds")
//    static List<Long> mapTagsToIds(List<Tag> tags) {
//        return tags != null ? tags.stream().map(Tag::getId).toList() : null;

//    }

//    @Named("idToTagList")
//    @IterableMapping(qualifiedByName = "idToTag")
//    List<Tag> idToTagList(List<Long> tagIds);


//    static List<Tag> mapIdsToTags(List<Long> tagIds) {
//        return tagIds != null ? tagIds.stream().map(id -> {
//            Tag tag = new Tag();
//            tag.setId(id);
//            return tag;
//        }).toList() : null;
//    }

//    @Named("mapIdsToPhotos")
//    static List<Photo> mapIdsToPhotos(List<Long> photoIds) {
//        return photoIds != null ? photoIds.stream().map(id -> {
//            Photo photo = new Photo();
//            photo.setId(id);
//            return photo;
//        }).toList() : null;
//    }

//    @Named("mapIdToCategory")
//    default Category mapIdToCategory(Long categoryId) {
//        // You can autowire CategoryService here or use the existing CategoryMapper
//        return categoryMapper.mapIdToCategory(categoryId, categoryService);
//    }
}
