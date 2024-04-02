package com.firma.business.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@WebAppConfiguration
class UserControllerTest {

    private final static String UserURL = "/api/business/user";

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getPersonalInfo() throws Exception {
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("username", "password",
                        AuthorityUtils.createAuthorityList("ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(principal);
        MvcResult result = mockMvc.perform(get(UserURL + "/get/info/jefe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", "kevin.prada"))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    void getUserName() throws Exception {
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("username", "password",
                        AuthorityUtils.createAuthorityList("ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(principal);
        MvcResult result = mockMvc.perform(get("/api/business/user/get/name")
                        .param("name", "sebasorjuela"))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}