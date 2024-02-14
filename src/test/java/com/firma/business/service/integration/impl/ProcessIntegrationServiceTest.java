package com.firma.business.service.integration.impl;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.request.ProcessRequest;
import com.firma.business.payload.response.DespachoResponse;
import com.firma.business.service.integration.intf.IProcessIntegrationService;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ProcessIntegrationServiceTest {
    @InjectMocks
    ProcessIntegrationService processIntegrationService;
    @Mock
    RestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProcess_returnsProcessRequest_whenApiCallIsSuccessful() throws ErrorIntegrationServiceException {
        ProcessRequest expectedProcessRequest = new ProcessRequest();
        when(restTemplate.getForEntity(anyString(), Mockito.eq(ProcessRequest.class)))
                .thenReturn(new ResponseEntity<>(expectedProcessRequest, HttpStatus.OK));

        ProcessRequest actualProcessRequest = processIntegrationService.getProcess("123");

        assertEquals(expectedProcessRequest, actualProcessRequest);
    }

    @Test
    void getProcess_throwsErrorIntegrationServiceException_whenApiCallFails() {
        when(restTemplate.getForEntity(anyString(), Mockito.eq(ProcessRequest.class)))
                .thenThrow(new RuntimeException("API call failed"));

        assertThrows(ErrorIntegrationServiceException.class, () -> processIntegrationService.getProcess("123"));
    }

    @Test
    void findUrlDespacho_returnsDespachoResponse_whenApiCallIsSuccessful() throws ErrorIntegrationServiceException {
        DespachoResponse expectedDespachoResponse = new DespachoResponse();
        when(restTemplate.getForEntity(anyString(), Mockito.eq(DespachoResponse.class)))
                .thenReturn(new ResponseEntity<>(expectedDespachoResponse, HttpStatus.OK));

        DespachoResponse actualDespachoResponse = processIntegrationService.findUrlDespacho("nombre");

        assertEquals(expectedDespachoResponse, actualDespachoResponse);
    }

    @Test
    void findUrlDespacho_throwsErrorIntegrationServiceException_whenApiCallFails() {
        when(restTemplate.getForEntity(anyString(), Mockito.eq(DespachoResponse.class)))
                .thenThrow(new RuntimeException("API call failed"));

        assertThrows(ErrorIntegrationServiceException.class, () -> processIntegrationService.findUrlDespacho("nombre"));
    }

    @Test
    void getAllProcess_returnsProcessRequest_whenApiCallIsSuccessful() throws ErrorIntegrationServiceException {
        ProcessRequest expectedProcessRequest = new ProcessRequest();
        when(restTemplate.getForEntity(anyString(), Mockito.eq(ProcessRequest.class)))
                .thenReturn(new ResponseEntity<>(expectedProcessRequest, HttpStatus.OK));

        ProcessRequest actualProcessRequest = processIntegrationService.getAllProcess("123");

        assertEquals(expectedProcessRequest, actualProcessRequest);
    }

    @Test
    void getAllProcess_throwsErrorIntegrationServiceException_whenApiCallFails() {
        when(restTemplate.getForEntity(anyString(), Mockito.eq(ProcessRequest.class)))
                .thenThrow(new RuntimeException("API call failed"));

        assertThrows(ErrorIntegrationServiceException.class, () -> processIntegrationService.getAllProcess("123"));
    }
}