package com.firma.business.implIntegration;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.request.ProcessRequest;
import com.firma.business.payload.response.DespachoResponse;
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
    void findUrlDespacho_returnsDespachoResponse_whenApiCallIsSuccessful() throws ErrorIntegrationServiceException {
        DespachoResponse expectedDespachoResponse = new DespachoResponse();
        when(restTemplate.getForEntity(anyString(), Mockito.eq(DespachoResponse.class)))
                .thenReturn(new ResponseEntity<>(expectedDespachoResponse, HttpStatus.OK));

        DespachoResponse actualDespachoResponse = processIntegrationService.findUrlDespacho("nombre", 2023);

        assertEquals(expectedDespachoResponse, actualDespachoResponse);
    }


    @Test
    void getAllProcess_returnsProcessRequest_whenApiCallIsSuccessful() throws ErrorIntegrationServiceException {
        ProcessRequest expectedProcessRequest = new ProcessRequest();
        when(restTemplate.getForEntity(anyString(), Mockito.eq(ProcessRequest.class)))
                .thenReturn(new ResponseEntity<>(expectedProcessRequest, HttpStatus.OK));

        ProcessRequest actualProcessRequest = processIntegrationService.getAllProcess("123");

        assertEquals(expectedProcessRequest, actualProcessRequest);
    }

}