package ua.tc.marketplace.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.AttributeValueKey;
import ua.tc.marketplace.model.dto.ad.AdAttributeCountDto;
import ua.tc.marketplace.model.dto.ad.AdAttributeDto;
import ua.tc.marketplace.model.entity.AdAttribute;

/** Mapper interface for converting AdAttribute entities to AdAttributeDto. */
@Mapper(config = MapperConfig.class)
public interface AdAttributeMapper {

  @Mapping(target = "name", source = "attribute.name")
  AdAttributeDto toDto(AdAttribute adAttribute);

  @Mapping(target = "name", source = "key.name")
  @Mapping(target = "value", source = "key.value")
  @Mapping(target = "count", source = "count")
  AdAttributeCountDto toDto(AttributeValueKey key, Long count);
}
