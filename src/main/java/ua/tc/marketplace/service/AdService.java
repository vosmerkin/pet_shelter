package ua.tc.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ua.tc.marketplace.model.AttributeValueKey;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.Category;

import java.util.Map;

/**
 * Service interface defining operations for managing advertisements. Includes methods for
 * retrieving, creating, updating, and deleting advertisements.
 */
public interface AdService {

  Page<Ad> findAll(Specification<Ad> specification, Pageable pageable);

  Ad save(Ad ad);

  void delete(Ad ad);

  Ad findAdById(Long adId);

  Map<AttributeValueKey, Long> countAdsByAdAttribute(Specification<Ad> specification);

  Long countAdsByCategory(Category category);
}
