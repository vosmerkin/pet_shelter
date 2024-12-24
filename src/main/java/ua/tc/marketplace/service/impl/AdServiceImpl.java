package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tc.marketplace.exception.ad.AdNotFoundException;
import ua.tc.marketplace.model.AttributeValueKey;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.AdAttribute;
import ua.tc.marketplace.repository.AdRepository;
import ua.tc.marketplace.service.AdService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the AdService interface providing CRUD operations for managing advertisements.
 * Uses AdRepository for database interactions, AdMapper for DTO mapping, and services for related
 * entities. Manages transactions and handles exceptions such as AdNotFoundException.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdServiceImpl implements AdService {

  private final AdRepository adRepository;

  @Transactional(readOnly = true)
  @Override
  public Page<Ad> findAll(Specification<Ad> specification, Pageable pageable) {
    return adRepository.findAll(specification, pageable);
  }

  @Override
  public Ad save(Ad ad) {
    return adRepository.save(ad);
  }

  @Override
  public void delete(Ad ad) {
    adRepository.delete(ad);
  }

    @Transactional(readOnly = true)
    @Override
    public Ad findAdById(Long adId) {
        return adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException(adId));
    }

    public Map<AttributeValueKey, Long> countAdsByAdAttribute(Specification<Ad> specification) {
        return adRepository.countAdsGroupedByAttribute(specification);
    }

  @Override
  public Long countAdsByCategory(Category category) {
    return adRepository.countByCategoryId(category.getId());
  }
}
