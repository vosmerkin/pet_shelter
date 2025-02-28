package ua.tc.marketplace.util.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.tag.CreateTagDto;
import ua.tc.marketplace.model.dto.tag.TagDto;
import ua.tc.marketplace.model.dto.tag.UpdateTagDto;
import ua.tc.marketplace.model.entity.Tag;
import ua.tc.marketplace.service.TagService;

import java.util.List;

/**
 * Mapper interface using MapStruct for converting between Tag entities and DTOs. Defines mappings
 * for converting Tag to TagDto.
 */
@Mapper(config = MapperConfig.class, uses = {TagService.class})
public interface TagMapper {

    TagDto toDto(Tag entity);

    Tag toEntity(CreateTagDto dto);

    void updateEntityFromDto(@MappingTarget Tag tag, UpdateTagDto tagDto);

    @Named("idToTag")
    Tag idToTag(Long id);

    @Named("idToTagList")
    @IterableMapping(qualifiedByName = "idToTag")
    List<Tag> idToTagList(List<Long> tagIds);

    @Named("mapTagToId")
    Long mapTagToId(TagDto dto);

    @Named("mapTagsToIds")
    @IterableMapping(qualifiedByName = "mapTagToId")
    List<Tag> mapTagsToIds(List<Long> tagIds);


//    @Named("idToTag")
//    default Tag idToTag(Long id) {
//        if (id == null) {
//            return null;
//        }
//        return tagRepository
//                .findById(id)
//                .orElseThrow(() -> new InvalidAttributeIdsException(Set.of(id)));
//    }
}
