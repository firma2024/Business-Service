package com.firma.business.service.integration.impl;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.request.ActuacionEmailRequest;
import com.firma.business.payload.request.ActuacionRequest;
import com.firma.business.payload.request.FindProcessRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ActuacionIntegrationServiceTest {

    @InjectMocks
    ActuacionIntegrationService actuacionIntegrationService;
    @Mock
    RestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findNewActuacionReturnsActuacionRequests() throws ErrorIntegrationServiceException {
        // Given
        FindProcessRequest findProcessRequest = new FindProcessRequest();
        ActuacionRequest actuacionRequest = new ActuacionRequest();
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ActuacionRequest[].class)
        )).thenReturn(new ResponseEntity<>(new ActuacionRequest[]{actuacionRequest}, HttpStatus.OK));

        // When
        var result = actuacionIntegrationService.findNewActuacion(Collections.singletonList(findProcessRequest));

        // Then
        assertEquals(Collections.singletonList(actuacionRequest), result);
    }

    @Test
    void findNewActuacionThrowsErrorIntegrationServiceException() {
        // Given
        FindProcessRequest findProcessRequest = new FindProcessRequest();
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ActuacionRequest[].class)
        )).thenThrow(new RestClientException("error"));

        // When & Then
        assertThrows(ErrorIntegrationServiceException.class, () -> actuacionIntegrationService.findNewActuacion(Collections.singletonList(findProcessRequest)));
    }

    @Test
    void sendEmailActuacionReturnsIntegers() throws ErrorIntegrationServiceException {
        // Given
        ActuacionEmailRequest actuacionEmailRequest = new ActuacionEmailRequest();
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Integer[].class)
        )).thenReturn(new ResponseEntity<>(new Integer[]{1}, HttpStatus.OK));

        // When
        var result = actuacionIntegrationService.sendEmailActuacion(Collections.singletonList(actuacionEmailRequest));

        // Then
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    void sendEmailActuacionThrowsErrorIntegrationServiceException() {
        // Given
        ActuacionEmailRequest actuacionEmailRequest = new ActuacionEmailRequest();
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Integer[].class)
        )).thenThrow(new RestClientException("error"));

        // When & Then
        assertThrows(ErrorIntegrationServiceException.class, () -> actuacionIntegrationService.sendEmailActuacion(Collections.singletonList(actuacionEmailRequest)));
    }

}