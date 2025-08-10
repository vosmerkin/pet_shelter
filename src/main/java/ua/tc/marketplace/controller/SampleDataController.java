package ua.tc.marketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.config.ApiURLs;
import ua.tc.marketplace.util.sampledata.SampleDataService;

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
  @PostMapping(ApiURLs.SAMPLE_DATA_ADD_ADS)
  public ResponseEntity<String> createAds(@RequestParam("count") int count) {
    log.info("Request to create {} ads ", count);
//    sampleDataService.addSampleAds(count);
    sampleDataService.addSampleData(count);
    return ResponseEntity.status(HttpStatus.CREATED).body(count + " ads created");
  }

}
