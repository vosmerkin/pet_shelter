package ua.tc.marketplace.facade.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tc.marketplace.exception.attribute.AdAttributesNotMatchCategoryException;
import ua.tc.marketplace.exception.attribute.AttributeNotFoundException;
import ua.tc.marketplace.exception.attribute.FailedToParseAdAttributesJsonException;
import ua.tc.marketplace.facade.AdFacade;
import ua.tc.marketplace.model.AttributeValueKey;
import ua.tc.marketplace.model.dto.ad.*;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.AdAttribute;
import ua.tc.marketplace.model.entity.Attribute;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.Location;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.service.AdService;
import ua.tc.marketplace.service.AuthenticationService;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.service.DistanceService;
import ua.tc.marketplace.service.LocationService;
import ua.tc.marketplace.service.PhotoStorageService;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.util.ad_filtering.FilterSpecificationFactory;
import ua.tc.marketplace.util.mapper.AdAttributeMapper;
import ua.tc.marketplace.util.mapper.AdMapper;

/**
 * AdFacadeImpl implements the AdFacade interface, providing concrete operations for managing ads in
 * the system.
 *
 * <p>This class is responsible for interacting with the underlying services and repositories to
 * handle ad-related operations, including creation, update, retrieval, and deletion of ads. It also
 * manages ad attributes and performs filtering based on criteria provided in the form of DTOs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdFacadeImpl implements AdFacade {

  private final ObjectMapper objectMapper = new ObjectMapper();

    private final AdService adService;
    private final AdMapper adMapper;
    private final AdAttributeMapper adAttributeMapper;
    private final PhotoStorageService photoService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final FilterSpecificationFactory filterSpecificationFactory;
    private final DistanceService distanceService;
    private final AuthenticationService authenticationService;
    private final LocationService locationService;

  @Override
  @Transactional
  public Page<AdDto> findAll(Map<String, String> filterCriteria, Pageable pageable) {
    Specification<Ad> specification = filterSpecificationFactory.getSpecification(filterCriteria);
    Page<AdDto> adDtoPage = adService.findAll(specification, pageable).map(adMapper::toAdDto);
    log.debug("adDtoPage.content : {}", adDtoPage.getContent());

    Optional<Location> optionalLocation1 =
        locationService.extractLocationFromParams(filterCriteria);

    if (optionalLocation1.isPresent() && !optionalLocation1.get().getAddress().isEmpty()) {
      Location location1 = optionalLocation1.get();
      log.debug("Location is present in request params: {}", location1);

      Optional<Location> optionalExistingLocation = locationService.findByParams(location1);
      Location finalLocation = location1;
      location1 = optionalExistingLocation.orElseGet(() -> locationService.save(finalLocation));
      log.debug("Location1 after database existence check : {}", location1);

      // Assign the updated page after distance calculation
      adDtoPage = distanceService.calculateDistance(location1, adDtoPage);
    } else {
      Optional<User> optionalUser = authenticationService.getAuthenticatedUser();
      if (optionalUser.isPresent()) {
        User authenticatedUser = optionalUser.get();
        log.debug("try to extract location from user: {}", authenticatedUser);

        // Assign the updated page after distance calculation
        adDtoPage = distanceService.calculateDistance(authenticatedUser.getLocation(), adDtoPage);
      }
    }

    return adDtoPage;
  }

  @Transactional(readOnly = true)
  @Override
  public AdDto findAdById(Long adId) {
    Ad ad = adService.findAdById(adId);
    return adMapper.toAdDto(ad);
  }

  @Override
  public AdDto createNewAd(CreateAdDto dto) {
    Ad ad = adMapper.getPrimitiveFields(dto);
    ad.setIsHot(false);

    User author = userService.findUserById(dto.authorId());

    Category category = categoryService.findCategoryById(dto.categoryId());

    List<AdAttributeRequestDto> adAttributeRequestDtos = parseJsonAdAttributes(dto);

    List<AdAttribute> adAttributes = mapToAdAttributes(adAttributeRequestDtos, category, ad);

    ad.setAdAttributes(adAttributes);
    ad.setAuthor(author);
    ad.setCategory(category);
    ad = adService.save(ad);

    photoService.saveAdPhotos(ad.getId(), dto.photoFiles());

    ad = adService.findAdById(ad.getId());
    return adMapper.toAdDto(ad);
  }

  @Override
  public AdDto updateAd(Long adId, UpdateAdDto dto) {
    Ad ad = adService.findAdById(adId);
    adMapper.updateAd(dto, ad);
    updateAdAttributes(dto, ad);

    Category category = categoryService.findCategoryById(dto.categoryId());
    User author = userService.findUserById(dto.authorId());
    ad.setCategory(category);
    ad.setAuthor(author);

    ad = adService.save(ad);
    return adMapper.toAdDto(ad);
  }

  @Override
  public void deleteAd(Long adId) {
    Ad ad = adService.findAdById(adId);
    photoService.deleteAllAdPhotos(ad);
    adService.delete(ad);
  }

  @Override
  public Long countAdsByCategory(Category category) {
    return adService.countAdsByCategory(category);
  }

  private AdAttribute getAdAttribute(Entry<Long, String> entry, Ad finalAd, Category category) {
    return new AdAttribute(
        null,
        finalAd,
        category.getAttributes().stream()
            .filter(attribute -> attribute.getId().equals(entry.getKey()))
            .findFirst()
            .orElseThrow(() -> new AttributeNotFoundException(entry.getKey())),
        entry.getValue());
  }

  private List<AdAttributeRequestDto> parseJsonAdAttributes(CreateAdDto dto) {
    try {
      return objectMapper.readValue(dto.adAttributes(), new TypeReference<>() {});
    } catch (IOException e) {
      throw new FailedToParseAdAttributesJsonException(dto.adAttributes());
    }
  }

  private List<AdAttribute> mapToAdAttributes(
      List<AdAttributeRequestDto> adAttributeRequestDtos, Category category, Ad ad) {

    Map<Long, String> attributeMap =
        adAttributeRequestDtos.stream()
            .collect(
                Collectors.toMap(AdAttributeRequestDto::attributeId, AdAttributeRequestDto::value));
    // Convert the list of attributes to a set of IDs
    Set<Long> requiredAttributeIds =
        category.getAttributes().stream().map(Attribute::getId).collect(Collectors.toSet());

    // Check if every key in attributeMap is present in attributeIds
    boolean allKeysPresent = requiredAttributeIds.containsAll(attributeMap.keySet());

    if (allKeysPresent) {
      return attributeMap.entrySet().stream()
          .map(entry -> getAdAttribute(entry, ad, category))
          .collect(Collectors.toList());
    } else {
      throw new AdAttributesNotMatchCategoryException(attributeMap.keySet(), requiredAttributeIds);
    }
  }

  private void updateAdAttributes(UpdateAdDto dto, Ad ad) {
    List<AdAttributeRequestDto> adAttributeRequestDtos = dto.adAttributes();
    List<AdAttribute> adAttributes = ad.getAdAttributes();

    // Step 1: Convert adAttributeRequestDtos to a Map<Long, String> for quick lookup
    Map<Long, String> attributeUpdates =
        adAttributeRequestDtos.stream()
            .collect(
                Collectors.toMap(AdAttributeRequestDto::attributeId, AdAttributeRequestDto::value));

        // Step 2: Update adAttributes list based on the attributeUpdates map
        adAttributes.forEach(
                adAttribute -> {
                    if (attributeUpdates.containsKey(adAttribute.getAttribute().getId())) {
                        adAttribute.setValue(attributeUpdates.get(adAttribute.getAttribute().getId()));
                    }
                });
    }

    public List<AdAttributeCountDto> countAdsByAdAttribute(Map<String, String> filterCriteria) {
        Specification<Ad> specification = filterSpecificationFactory.getSpecification(filterCriteria);
        Map<AttributeValueKey, Long> attributeCounts = adService.countAdsByAdAttribute(specification);

        return attributeCounts.entrySet().stream()
                .map(entry -> adAttributeMapper.toDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
