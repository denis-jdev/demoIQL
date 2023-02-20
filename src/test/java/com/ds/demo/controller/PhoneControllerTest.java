package com.ds.demo.controller;

import com.ds.demo.dto.PhoneDto;
import com.ds.demo.security.Http401UnauthorizedEntryPoint;
import com.ds.demo.security.JwtRequestFilter;
import com.ds.demo.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PhoneController.class)
@Import({PhoneController.class})
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
class PhoneControllerTest {
    private final Long senderId = 1L;
    private final String baseUrl = "/api/v1/phone";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PhoneService phoneService;
    @MockBean
    private Authentication auth;
    @MockBean
    private Http401UnauthorizedEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    public void initSecurityContext() {
        Mockito.when(auth.getPrincipal()).thenReturn(senderId);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void deletePhoneSuccess() throws Exception {
        final Long phoneId = 1L;
        PhoneDto dto = new PhoneDto(phoneId, "79002003020");
        ObjectMapper mapper = new ObjectMapper();

        String requestJson = mapper.writeValueAsString(dto);
        mockMvc
                .perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deletePhoneError400_invalidPhone() throws Exception {
        final Long phoneId = 1L;
        PhoneDto dto = new PhoneDto(phoneId, "790020030");
        ObjectMapper mapper = new ObjectMapper();

        String requestJson = mapper.writeValueAsString(dto);
        mockMvc
                .perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePhoneError400_invalidId() throws Exception {
        PhoneDto dto = new PhoneDto(null, "790020030");
        ObjectMapper mapper = new ObjectMapper();

        String requestJson = mapper.writeValueAsString(dto);
        mockMvc
                .perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }
}