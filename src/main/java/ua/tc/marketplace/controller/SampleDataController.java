package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.config.ApiURLs;
import ua.tc.marketplace.model.dto.category.CategoryCountedDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.dto.category.CreateCategoryDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryDto;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.util.SampleDataService;
import ua.tc.marketplace.util.openapi.CategoryOpenApi;

/**

 */
@RestController
//@RequestMapping("/api/v1/sample_data")
@RequestMapping(ApiURLs.SAMPLE_DATA_BASE)

@RequiredArgsConstructor
@Slf4j
public class SampleDataController {

  private final SampleDataService sampleDataService;

//  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<String> createAds(@RequestParam("count") int count) {
    log.info("Request to create {} ads ", count);
    sampleDataService.addSampleAds(count);
    return ResponseEntity.status(HttpStatus.CREATED).body(count + " ads created");
  }

}
