package ua.tc.marketplace.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.tc.marketplace.data.AdTestData.getAttributeDtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import ua.tc.marketplace.data.AdTestData;
import ua.tc.marketplace.facade.AdFacade;
import ua.tc.marketplace.model.dto.ad.AdAttributeCountDto;
import ua.tc.marketplace.model.dto.ad.AdAttributeDto;
import ua.tc.marketplace.model.dto.ad.AdAttributeRequestDto;
import ua.tc.marketplace.model.dto.ad.AdDto;
import ua.tc.marketplace.model.dto.ad.AdFilterPageAndAttributesCountDto;
import ua.tc.marketplace.model.dto.ad.CreateAdDto;
import ua.tc.marketplace.model.dto.ad.UpdateAdDto;
import ua.tc.marketplace.model.entity.Location;
import ua.tc.marketplace.model.entity.Photo;

@ExtendWith(MockitoExtension.class)
class AdControllerTest {

    @Mock
    private AdFacade adFacade;

    private AdController controller;

    // Test data
    private Pageable defaultPageable;
    private Map<String, String> defaultParams;

    private AdDto sampleAdDto;
    private CreateAdDto sampleCreateAdDto;
    private UpdateAdDto sampleUpdateAdDto;
    private Page<AdDto> sampleAdPage;
    private List<AdAttributeCountDto> sampleAttributeCounts;

    @BeforeEach
    void setUp() {
        controller = new AdController(adFacade);

        defaultPageable = PageRequest.of(0, 10);
        defaultParams = Map.of("category", "1"); // dog

                sampleAdDto = AdTestData.getAdDto();
        sampleCreateAdDto = AdTestData.getCreateAdDto();
        sampleUpdateAdDto = AdTestData.getUpdateAdDto();

        sampleAdPage = new PageImpl<>(List.of(sampleAdDto));
        sampleAttributeCounts = List.of(
                new AdAttributeCountDto("breed", "Лабрадор-ретривер", 5L),
                new AdAttributeCountDto("age", "0-1 р", 12L)
        );
    }

    // === TEST METHODS (unchanged logic, but now with pet data) ===

    @Test
    void getAllAds_shouldCallAdFacadeAndReturnPage() {
        when(adFacade.findAll(defaultParams, defaultPageable)).thenReturn(sampleAdPage);

        ResponseEntity<Page<AdDto>> response = controller.getAllAds(defaultParams, defaultPageable);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(sampleAdPage);
        verify(adFacade).findAll(defaultParams, defaultPageable);
    }

    @Test
    void getAllAdsWithAttributeItemCount_shouldReturnCombinedDto() {
        when(adFacade.findAll(defaultParams, defaultPageable)).thenReturn(sampleAdPage);
        when(adFacade.countAdsByAdAttribute(defaultParams)).thenReturn(sampleAttributeCounts);

        ResponseEntity<AdFilterPageAndAttributesCountDto> response =
                controller.getAllAdsWithAttributeItemCount(defaultParams, defaultPageable);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        AdFilterPageAndAttributesCountDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.page()).isEqualTo(sampleAdPage);
        assertThat(body.listOfAttributeCounts()).isEqualTo(sampleAttributeCounts);
        verify(adFacade).findAll(defaultParams, defaultPageable);
        verify(adFacade).countAdsByAdAttribute(defaultParams);
    }

    @Test
    void getAdById_shouldReturnAdDto() {
        Long adId = 1L;
        when(adFacade.findAdById(adId)).thenReturn(sampleAdDto);

        ResponseEntity<AdDto> response = controller.getAdById(adId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(sampleAdDto);
        verify(adFacade).findAdById(adId);
    }

    @Test
    void createNewAd_shouldCallAdFacadeWithDto() {
        // Simulate created ad (with ID assigned)
        AdDto createdAd = new AdDto(
                10L, 101L,
                "Молодий лабрадор - безплатно!",
                "Шукає люблячу сім'ю...",
                BigDecimal.ZERO,
                null, null, null, null,
                1L,
                List.of(
                        new AdAttributeDto("breed", "Лабрадор-ретривер"),
                        new AdAttributeDto("pet_name", "Рекс")
                ),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
        when(adFacade.createNewAd(sampleCreateAdDto)).thenReturn(createdAd);

        ResponseEntity<AdDto> response = controller.createNewAd(sampleCreateAdDto);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(createdAd);
        verify(adFacade).createNewAd(sampleCreateAdDto);
    }

    @Test
    void updateAd_shouldCallAdFacadeWithIdAndDto() {
        Long adId = 1L;
        AdDto updatedAd = new AdDto(
                adId, 101L,
                "Оновлений опис — золотистий ретривер!",
                "Чудова собака...",
                BigDecimal.ZERO,
                null, null, null, null,
                1L,
                List.of(
                        new AdAttributeDto("breed", "Золотистий ретривер"),
                        new AdAttributeDto("pet_name", "Белла")
                ),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                false
        );
        when(adFacade.updateAd(eq(adId), any(UpdateAdDto.class))).thenReturn(updatedAd);

        ResponseEntity<AdDto> response = controller.updateAd(adId, sampleUpdateAdDto);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(updatedAd);
        verify(adFacade).updateAd(adId, sampleUpdateAdDto);
    }

    @Test
    void deleteAd_shouldCallAdFacadeAndReturnOk() {
        Long adId = 1L;

        ResponseEntity<Void> response = controller.deleteAd(adId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(adFacade).deleteAd(adId);
    }
}