package ua.tc.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.tc.marketplace.config.ApiURLs;
import ua.tc.marketplace.model.dto.ad.AdDto;
import ua.tc.marketplace.model.dto.ad.CreateAdDto;
import ua.tc.marketplace.model.dto.ad.UpdateAdDto;
import ua.tc.marketplace.service.AdService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
public class AdControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    private AdDto testAdDto;
    private CreateAdDto createAdDto;
    private UpdateAdDto updateAdDto;

    @BeforeEach
    void setUp(){

    }

    @Test
    @WithAnonymousUser
    void publicEndpoints_ShouldBeAccessibleAnonymously() throws Exception {
        mockMvc.perform(get(ApiURLs.AD_BASE+ApiURLs.AD_GET_ALL))
                .andExpect(status().isOk());

        mockMvc.perform(get(ApiURLs.AD_BASE+ApiURLs.AD_GET_ALL_COUNTED))
                .andExpect(status().isOk());

        mockMvc.perform(get(ApiURLs.AD_BASE+ApiURLs.AD_GET_BY_ID.replace("{adId}", "12345")))
                .andExpect(status().isOk());
    }
}
