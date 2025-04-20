package ua.tc.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.tc.marketplace.config.ApiURLs;
import ua.tc.marketplace.config.SecurityConfig;
import ua.tc.marketplace.facade.AdFacade;
import ua.tc.marketplace.jwtAuth.JwtAuthorizationFilter;
import ua.tc.marketplace.jwtAuth.JwtUtil;
import ua.tc.marketplace.model.dto.ad.AdDto;
import ua.tc.marketplace.model.dto.ad.CreateAdDto;
import ua.tc.marketplace.model.dto.ad.UpdateAdDto;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.service.AdService;
import ua.tc.marketplace.service.impl.UserDetailsServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(AdController.class)
@AutoConfigureMockMvc
//@Import({SecurityConfig.class, UserDetailsServiceImpl.class})
//@EnableJpaRepositories(basePackages = "ua.tc.marketplace.repository")
@SpringBootTest
public class AdControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    @MockBean
    private AdFacade adFacade;

    @MockBean
    private JwtUtil jwtUtil;

//    @MockBean
//    private JwtAuthorizationFilter jwtAuthorizationFilter;

//    @MockBean
//    private UserDetailsService userDetailsService;


    private AdDto testAdDto;
    private CreateAdDto createAdDto;
    private UpdateAdDto updateAdDto;

    @BeforeEach
    void setUp() {
        createAdDto = new CreateAdDto(
                1L,
                "Sample Ad",
                "This is a sample ad",
                BigDecimal.valueOf(100.00),
                null,
                null,
                1L);
        updateAdDto = new UpdateAdDto(
                1L, // Mock author ID
                "Updated Title",
                "Updated Description",
                BigDecimal.valueOf(200.00),
                2L, // Updated Category ID
                Collections.emptyList());

    }

    @Test
    @WithAnonymousUser
    void publicEndpoints_ShouldBeAccessibleAnonymously() throws Exception {
        mockMvc.perform(get(ApiURLs.AD_BASE + ApiURLs.AD_GET_ALL))
                .andExpect(status().isOk());

        mockMvc.perform(get(ApiURLs.AD_BASE + ApiURLs.AD_GET_ALL_COUNTED))
                .andExpect(status().isOk());

        mockMvc.perform(get(ApiURLs.AD_BASE + ApiURLs.AD_GET_BY_ID.replace("{adId}", "12345")))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void protectedEndpoints_ShouldRejectAnonymousAccess() throws Exception {
        String response = mockMvc.perform(post(ApiURLs.AD_BASE + ApiURLs.AD_CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAdDto)))
//                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response: " + response + " ;;");;

        mockMvc.perform(put("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAdDto)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/v1/article/1"))
                .andExpect(status().isUnauthorized());
    }
}
