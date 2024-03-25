package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.implData.StorageDataService;
import com.firma.business.payload.response.ActuacionDocumentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class StorageDataServiceTest {

    @InjectMocks
    StorageDataService storageDataService;
    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadPhotoShouldReturnExpectedResponse() throws Exception {
        MultipartFile file = new MockMultipartFile("image", "Hello, World!".getBytes());
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(mockResponse);

        String response = storageDataService.uploadPhoto(file, 1);

        assertEquals("Success", response);
    }


    @Test
    void downloadPhoto() throws ErrorDataServiceException {
        // Arrange
        Integer usuarioId = 1;
        byte[] expectedPhoto = {0x01, 0x02, 0x03}; // example byte array
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(expectedPhoto, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(new ParameterizedTypeReference<byte[]>() {})))
                .thenReturn(responseEntity);

        // Act
        byte[] result = storageDataService.downloadPhoto(usuarioId);

        // Assert
        assertArrayEquals(expectedPhoto, result);
    }


    @Test
    void uploadDocumentShouldReturnExpectedResponse() throws Exception {
        MultipartFile file = new MockMultipartFile("doc", "Hello, World!".getBytes());
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(mockResponse);

        String response = storageDataService.uploadDocument(file, 1);

        assertEquals("Success", response);
    }


    @Test
    void downloadDocument() throws ErrorDataServiceException {
        // Arrange
        Integer actuacionId = 1;
        byte[] expectedDocument = {0x01, 0x02, 0x03}; // example byte array
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(expectedDocument, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(new ParameterizedTypeReference<byte[]>() {}))
        ).thenReturn(responseEntity);

        // Act
        byte[] result = storageDataService.downloadDocument(actuacionId);

        // Assert
        assertArrayEquals(expectedDocument, result);
    }


    @Test
    void downloadAllDocumentsShouldReturnExpectedResponse() throws Exception {
        // Arrange
        Integer procesoId = 1;
        ActuacionDocumentResponse[] expectedDocuments = new ActuacionDocumentResponse[]{};
        ResponseEntity<ActuacionDocumentResponse[]> responseEntity = new ResponseEntity<>(expectedDocuments, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ActuacionDocumentResponse[].class)))
                .thenReturn(responseEntity);

        // Act
        Set<ActuacionDocumentResponse> result = storageDataService.downloadAllDocuments(procesoId);

        // Assert
        assertEquals(Set.of(expectedDocuments), result);
    }



}