package ua.tc.marketplace.facade;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.tc.marketplace.model.dto.ad.AdAttributeCountDto;
import ua.tc.marketplace.model.dto.ad.AdDto;
import ua.tc.marketplace.model.dto.ad.CreateAdDto;
import ua.tc.marketplace.model.dto.ad.UpdateAdDto;
import ua.tc.marketplace.model.entity.Category;

/**
 * AdFacade defines the interface for operations related to ads within the system.
 *
 * <p>This interface includes methods for querying, creating, updating, and deleting ads. It
 * provides a high-level abstraction for ad management, allowing for interaction with ad entities
 * through data transfer objects (DTOs).
 */
public interface AdFacade {
  Page<AdDto> findAll(Map<String, String> filterCriteria, Pageable pageable);

  AdDto findAdById(Long adId);

  AdDto createNewAd(CreateAdDto dto);

  AdDto updateAd(Long adId, UpdateAdDto dto);

  void deleteAd(Long adId);

  List<AdAttributeCountDto> countAdsByAdAttribute(Map<String, String> filterCriteria);

  Long countAdsByCategory(Category category);
}
