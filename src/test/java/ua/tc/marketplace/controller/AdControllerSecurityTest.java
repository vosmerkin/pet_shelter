package ua.tc.marketplace.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.tc.marketplace.data.AdTestData;
import ua.tc.marketplace.facade.AdFacade;
import ua.tc.marketplace.model.dto.ad.UpdateAdDto;
import ua.tc.marketplace.service.SecurityService;

@WebMvcTest(AdController.class)
class AdControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdFacade adFacade;

    @MockBean
    private SecurityService securityService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String AD_BASE = "/api/v1/ad";
    private static final Long EXISTING_AD_ID = 123L;

    // ======================
    // CREATE AD (POST /ad)
    // ======================
    
    
    @Test
    @WithAnonymousUser
    void createNewAd_shouldReturn401_whenUnauthenticated() throws Exception {
        MockMultipartFile title = new MockMultipartFile("title", "", "text/plain", "Test Ad".getBytes());
        MockMultipartFile description = new MockMultipartFile("description", "", "text/plain", "Desc".getBytes());
        MockMultipartFile price = new MockMultipartFile("price", "", "text/plain", "0".getBytes());
        MockMultipartFile authorId = new MockMultipartFile("authorId", "", "text/plain", "1".getBytes());
        MockMultipartFile categoryId = new MockMultipartFile("categoryId", "", "text/plain", "1".getBytes());
        MockMultipartFile adAttributes = new MockMultipartFile("adAttributes", "", "text/plain",
                AdTestData.getAdAttributeJson().getBytes());
        MockMultipartFile photo = new MockMultipartFile("photoFiles", "test.jpg", "image/jpeg", "fake image".getBytes());

        mockMvc.perform(multipartPostWithFiles(title, description, price, authorId, categoryId, adAttributes, photo)
                        .with(csrf()))
                .andExpect(status().isUnauthorized()); // 401
    }

    @Test
    @WithMockUser
    void createNewAd_shouldReturn200_whenAuthenticated() throws Exception {
        MockMultipartFile title = new MockMultipartFile("title", "", "text/plain", "Test Ad".getBytes());
        MockMultipartFile description = new MockMultipartFile("description", "", "text/plain", "Desc".getBytes());
        MockMultipartFile price = new MockMultipartFile("price", "", "text/plain", "0".getBytes());
        MockMultipartFile authorId = new MockMultipartFile("authorId", "", "text/plain", "1".getBytes());
        MockMultipartFile categoryId = new MockMultipartFile("categoryId", "", "text/plain", "1".getBytes());
        MockMultipartFile adAttributes = new MockMultipartFile("adAttributes", "", "text/plain",
                AdTestData.getAdAttributeJson().getBytes());
        MockMultipartFile photo = new MockMultipartFile("photoFiles", "test.jpg", "image/jpeg", "fake image".getBytes());


        mockMvc.perform(multipartPostWithFiles(title, description, price, authorId, categoryId, adAttributes, photo)
                        .with(csrf()))
                .andExpect(status().isOk()); // 200
    }

    
    // ======================
    // UPDATE AD (PUT /ad/{id})
    // ======================

    @Test
    @WithAnonymousUser
    void updateAd_shouldReturn401_whenUnauthenticated() throws Exception {
        UpdateAdDto dto = AdTestData.getUpdateAdDto();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(AD_BASE + "/" + EXISTING_AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isUnauthorized()); // 401
    }

    @Test
    @WithMockUser
    void updateAd_shouldReturn403_whenNotOwner() throws Exception {
        // Simulate: user is authenticated but NOT owner
        when(securityService.hasAnyRoleAndAdOwnership(EXISTING_AD_ID)).thenReturn(false);

        UpdateAdDto dto = AdTestData.getUpdateAdDto();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(AD_BASE + "/" + EXISTING_AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isForbidden()); // 403
    }

    @Test
    @WithMockUser
    void updateAd_shouldReturn200_whenOwner() throws Exception {
        when(securityService.hasAnyRoleAndAdOwnership(EXISTING_AD_ID)).thenReturn(true);

        UpdateAdDto dto = AdTestData.getUpdateAdDto();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(AD_BASE + "/" + EXISTING_AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isOk()); // 200
    }

    // ======================
    // DELETE AD (DELETE /ad/{id})
    // ======================

    @Test
    @WithAnonymousUser
    void deleteAd_shouldReturn401_whenUnauthenticated() throws Exception {
        mockMvc.perform(delete(AD_BASE + "/" + EXISTING_AD_ID)
                        .with(csrf()))
                .andExpect(status().isUnauthorized()); // 401
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteAd_shouldReturn200_whenAdmin() throws Exception {
        mockMvc.perform(delete(AD_BASE + "/" + EXISTING_AD_ID)
                        .with(csrf()))
                .andExpect(status().isOk()); // 200
    }

    @Test
    @WithMockUser
    void deleteAd_shouldReturn403_whenNotOwnerAndNotAdmin() throws Exception {
        when(securityService.hasAnyRoleAndAdOwnership(EXISTING_AD_ID)).thenReturn(false);

        mockMvc.perform(delete(AD_BASE + "/" + EXISTING_AD_ID)
                        .with(csrf()))
                .andExpect(status().isForbidden()); // 403
    }

    @Test
    @WithMockUser
    void deleteAd_shouldReturn200_whenOwner() throws Exception {
        when(securityService.hasAnyRoleAndAdOwnership(EXISTING_AD_ID)).thenReturn(true);

        mockMvc.perform(delete(AD_BASE + "/" + EXISTING_AD_ID)
                        .with(csrf()))
                .andExpect(status().isOk()); // 200
    }

    // --- Helper for multipart POST ---

    private MockHttpServletRequestBuilder multipartPostWithFiles(MockMultipartFile... files) {
        return MockMvcRequestBuilders.multipart(AD_BASE)
                .file(files[0])                
                .file(files[1])
                .file(files[2])
                .file(files[3])
                .file(files[4])
                .file(files[5])
                .file(files[6]);
    }
}