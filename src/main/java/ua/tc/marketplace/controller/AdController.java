package ua.tc.marketplace.controller;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.tc.marketplace.config.ApiURLs;
import ua.tc.marketplace.facade.AdFacade;
import ua.tc.marketplace.model.dto.ad.*;
import ua.tc.marketplace.util.openapi.AdOpenApi;

/**
 * Controller class for managing advertisements via REST API endpoints. Handles operations such as
 * retrieving, creating, updating, and deleting advertisements.
 *
 * <p>Endpoints:
 *
 * <ul>
 *   <li>GET /api/v1/ad: Retrieves a pageable list of all advertisements.
 *   <li>GET /api/v1/ad/{adId}: Retrieves an advertisement by its unique identifier.
 *   <li>POST /api/v1/ad: Creates a new advertisement based on the provided data.
 *   <li>PUT /api/v1/ad/{adId}: Updates an existing advertisement with the provided data.
 *   <li>DELETE /api/v1/ad/{adId}: Deletes an advertisement by its unique identifier.
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiURLs.AD_BASE)
//@ConfigurationProperties(prefix = "external.api.ad")
public class AdController implements AdOpenApi {

    private final AdFacade adFacade;

    @Override
    @GetMapping
    public ResponseEntity<Page<AdDto>> getAllAds(
            @RequestParam Map<String, String> params, @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(adFacade.findAll(params, pageable));
    }

    @GetMapping("/counted")
    public ResponseEntity<AdFilterPageAndAttributesCountDto> getAllAdsWithAttributeItemCount(
            @RequestParam Map<String, String> params,
            @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new AdFilterPageAndAttributesCountDto(
                adFacade.findAll(params, pageable),
                adFacade.countAdsByAdAttribute(params)
                )
        );
    }

    @Override
    @GetMapping("/{adId}")
    public ResponseEntity<AdDto> getAdById(@PathVariable Long adId) {
        return ResponseEntity.ok(adFacade.findAdById(adId));
    }

    @Override
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AdDto> createNewAd(@ModelAttribute @Valid CreateAdDto dto) {
        return ResponseEntity.ok(adFacade.createNewAd(dto));
    }

    @Override
    @PutMapping("/{adId}")
    @PreAuthorize("@securityService.hasAnyRoleAndAdOwnership(#adId)")
    public ResponseEntity<AdDto> updateAd(@PathVariable Long adId, @RequestBody UpdateAdDto dto) {
        return ResponseEntity.ok(adFacade.updateAd(adId, dto));
    }

    @Override
    @DeleteMapping("/{adId}")
    @PreAuthorize("hasAuthority('ADMIN') or @securityService.hasAnyRoleAndAdOwnership(#adId)")
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId) {
        adFacade.deleteAd(adId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
