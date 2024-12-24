package ua.tc.marketplace.repository;

import org.springframework.data.jpa.domain.Specification;
import ua.tc.marketplace.model.AttributeValueKey;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.AdAttribute;

import java.util.List;
import java.util.Map;

/**
 * Custom repository interface for managing advertisements.
 *
 * <p>This repository adds method for counting number of ads by each attribute to show in filter.
 */
public interface AdCustomRepository  {
    Map<AttributeValueKey, Long> countAdsGroupedByAttribute(Specification<Ad> specification);
}
