package com.firma.business.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.business.payload.request.ProcessBusinessRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
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
class ProcessControllerTest {

    private final static String ProcessURL = "/api/business/process";

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void getInfoProcess() throws Exception{
        String numberProcess = "11001400305420210000800";
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("username", "password",
                        AuthorityUtils.createAuthorityList("ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(principal);
        MvcResult result = mockMvc.perform(get(ProcessURL + "/get/info")
                        .param("numberProcess", numberProcess)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    void addProcessTest() throws Exception {
        ProcessBusinessRequest processRequest = ProcessBusinessRequest.builder()
                .numeroRadicado("11001400305420210000800")
                .idAbogado(50)
                .build();
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("username", "password",
                        AuthorityUtils.createAuthorityList("ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(principal);
        String processRequestJson = new ObjectMapper().writeValueAsString(processRequest);
        MvcResult result = mockMvc.perform(post(ProcessURL + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(processRequestJson))
                .andReturn();
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }
}