package ua.tc.marketplace.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.tag.CreateTagDto;
import ua.tc.marketplace.model.dto.tag.TagDto;
import ua.tc.marketplace.model.dto.tag.UpdateTagDto;
import ua.tc.marketplace.model.entity.Tag;
import ua.tc.marketplace.service.TagService;

/**
 * Mapper interface using MapStruct for converting between Tag entities and DTOs. Defines mappings
 * for converting Tag to TagDto.
 */
@Mapper(config = MapperConfig.class, uses = {TagService.class})
public interface TagMapper {

    TagDto toDto(Tag entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    Tag toEntity(CreateTagDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    void updateEntityFromDto(@MappingTarget Tag tag, UpdateTagDto tagDto);
}
