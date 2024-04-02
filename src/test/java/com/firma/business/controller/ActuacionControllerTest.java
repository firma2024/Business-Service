package com.firma.business.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@WebAppConfiguration
class ActuacionControllerTest {
    private final static String ActuacionURL = "/api/business/actuacion";

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getActuacion() throws Exception {
        String idActuacion = "17";
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("username", "password",
                        AuthorityUtils.createAuthorityList("ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(principal);
        MvcResult result = mockMvc.perform(get(ActuacionURL + "/get")
                        .param("id", idActuacion)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}