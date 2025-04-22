//package ua.tc.marketplace.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.tc.marketplace.config.ApiURLs;
//import ua.tc.marketplace.facade.AdFacade;
//import ua.tc.marketplace.jwtAuth.JwtUtil;
//import ua.tc.marketplace.model.dto.ad.AdDto;
//import ua.tc.marketplace.model.dto.ad.CreateAdDto;
//import ua.tc.marketplace.model.dto.ad.UpdateAdDto;
//import ua.tc.marketplace.model.entity.User;
//import ua.tc.marketplace.model.enums.UserRole;
//import ua.tc.marketplace.service.AdService;
//import ua.tc.marketplace.service.SecurityService;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
////@WebMvcTest(AdController.class)
//@AutoConfigureMockMvc
////@Import({SecurityConfig.class, UserDetailsServiceImpl.class})
////@EnableJpaRepositories(basePackages = "ua.tc.marketplace.repository")
//@SpringBootTest
//public class AdControllerSecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private AdService adService;
//
//    @MockBean
//    private AdFacade adFacade;
//
//    //    @MockBean
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @MockBean
//    private SecurityService securityService;
//
////    @MockBean
////    private JwtAuthorizationFilter jwtAuthorizationFilter;
//
////    @MockBean
////    private UserDetailsService userDetailsService;
//
//
//    private AdDto testAdDto;
//    private CreateAdDto createAdDto;
//    private UpdateAdDto updateAdDto;
//    private User user;
//    private User adminUser;
//    private String token;
//    private String adminToken;
//
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setId(1L);
//        user.setFirstName("Taras");
//        user.setLastName("Shevchenko");
//        user.setEmail("taras@shevchenko.ua");
//        user.setUserRole(UserRole.USER);
//
//        token = jwtUtil.createToken(user);
//
//        adminUser = new User();
//        adminUser.setId(2L);
//        adminUser.setFirstName("TarasAdmin");
//        adminUser.setLastName("Shevchenko1");
//        adminUser.setEmail("taras_admin@shevchenko.ua");
//        adminUser.setUserRole(UserRole.ADMIN);
//
//        adminToken = jwtUtil.createToken(adminUser);
//
//        createAdDto = new CreateAdDto(
//                1L,
//                "Sample Ad",
//                "This is a sample ad",
//                BigDecimal.valueOf(100.00),
//                null,
//                null,
//                1L);
//        updateAdDto = new UpdateAdDto(
//                1L, // Mock author ID
//                "Updated Title",
//                "Updated Description",
//                BigDecimal.valueOf(200.00),
//                2L, // Updated Category ID
//                Collections.emptyList());
//
//    }
//
//    @Test
//    @WithAnonymousUser
//    void publicEndpoints_ShouldBeAccessibleAnonymously() throws Exception {
//        mockMvc.perform(get(ApiURLs.AD_BASE + ApiURLs.AD_GET_ALL))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(ApiURLs.AD_BASE + ApiURLs.AD_GET_ALL_COUNTED))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(ApiURLs.AD_BASE + ApiURLs.AD_GET_BY_ID,1L))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithAnonymousUser
//    void protectedEndpoints_ShouldRejectAnonymousAccess() throws Exception {
//        mockMvc.perform(post(ApiURLs.AD_BASE + ApiURLs.AD_CREATE)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createAdDto)))
//                .andExpect(status().isUnauthorized());
//
//        mockMvc.perform(put(ApiURLs.AD_BASE + ApiURLs.AD_UPDATE,1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateAdDto)))
//                .andExpect(status().isUnauthorized());
//
//        mockMvc.perform(delete(ApiURLs.AD_BASE + ApiURLs.AD_DELETE,1L))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @WithMockUser(username = "owner", roles = {"USER"})
//    void updateAd_WhenUserIsOwner_ShouldSucceed() throws Exception {
//        // Arrange
//        Long adId = 1L;
//        // Mock security service to return true (user is owner)
//        when(securityService.hasAnyRoleAndOwnership(adId)).thenReturn(true);
//
//        // Act & Assert
//        mockMvc.perform(put(ApiURLs.AD_BASE + ApiURLs.AD_UPDATE,adId)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateAdDto)))
//                .andExpect(status().isOk());
//
//        // Verify the security service was called with the correct path variable
//        verify(securityService).hasAnyRoleAndOwnership(adId);
//        // Verify the facade was called with correct parameters
//        verify(adFacade).updateAd(eq(adId), any(UpdateAdDto.class));
//    }
//
//    @Test
////    @WithMockUser(username = "notOwner", roles = {"USER"})
//    void updateAd_WhenUserIsNotOwner_ShouldFail() throws Exception {
//        // Arrange
//        Long adId = 1L;
//        // Mock security service to return false (user is not owner)
//        when(securityService.hasAnyRoleAndOwnership(adId)).thenReturn(false);
//
//        // Act & Assert
//        mockMvc.perform(put(ApiURLs.AD_BASE + ApiURLs.AD_UPDATE,adId)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateAdDto)))
//                .andExpect(status().isForbidden());
//
//        // Verify the security service was called
//        verify(securityService).hasAnyRoleAndOwnership(adId);
//        // Verify the facade was never called
//        verify(adFacade, never()).updateAd(anyLong(), any(UpdateAdDto.class));
//    }
//
//    @Test
////    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void updateAd_WhenUserIsAdmin_ShouldStillRequireOwnership() throws Exception {
//
//        // Arrange
//        Long adId = 1L;
//        // This tests that even admins need to be owners unless explicitly allowed
//        // If your security logic treats admins differently, adjust this test
//        when(securityService.hasAnyRoleAndOwnership(adId)).thenReturn(false);
//
//        // Act & Assert
//        mockMvc.perform(put(ApiURLs.AD_BASE + ApiURLs.AD_UPDATE,adId)
//                        .header("Authorization", "Bearer " + adminToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateAdDto)))
//                .andExpect(status().isForbidden());
//
//        verify(securityService).hasAnyRoleAndOwnership(adId);
//        verify(adFacade, never()).updateAd(anyLong(), any(UpdateAdDto.class));
//    }
//}
