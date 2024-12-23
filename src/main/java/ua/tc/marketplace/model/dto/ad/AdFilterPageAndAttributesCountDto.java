package ua.tc.marketplace.model.dto.ad;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a page ads and list of attributes with counts of items.
 */
public record AdFilterPageAndAttributesCountDto(
        Page<AdDto> page,
        List<AdAttributeCountDto> listOfAttributeCounts) {
}