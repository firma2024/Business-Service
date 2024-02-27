package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.response.ActuacionDocumentResponse;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.service.data.intf.IStorageDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StorageServiceTest {

    @InjectMocks
    StorageService storageService;
    @Mock
    IStorageDataService storageDataService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadPhoto() throws IOException, ErrorDataServiceException {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        Integer usuarioId = 1;
        when(storageDataService.uploadPhoto(file, usuarioId)).thenReturn("foto cargada");
        MessageResponse response = storageService.uploadPhoto(file, usuarioId);

        assertEquals ("foto cargada", response.getMessage());
    }

    @Test
    void downloadPhoto() throws ErrorDataServiceException {
        Integer usuarioId = 1;
        byte[] data = new byte[10];
        when(storageDataService.downloadPhoto(usuarioId)).thenReturn(data);

        byte[] response = storageService.downloadPhoto(usuarioId);
        assertEquals (data, response);
    }

    @Test
    void uploadDocument() throws ErrorDataServiceException, IOException {
        MultipartFile file = new MockMultipartFile("file", "doc.pdf", "application/pdf", "el doc".getBytes());
        Integer actuacionId = 10;

        when(storageDataService.uploadDocument(file, actuacionId)).thenReturn("Documento cargado");
        MessageResponse response = storageService.uploadDocument(file, actuacionId);

        assertEquals("Documento cargado", response.getMessage());
    }

    @Test
    void downloadDocument() throws ErrorDataServiceException {
        Integer actuacionId = 1;
        byte[] data = new byte[10];
        when(storageDataService.downloadDocument(actuacionId)).thenReturn(data);

        byte[] response = storageService.downloadDocument(actuacionId);
        assertEquals (data, response);
    }

    @Test
    void downloadAllDocumentsSuccessfully() throws ErrorDataServiceException {
        ActuacionDocumentResponse actuacionDocumentResponse = ActuacionDocumentResponse.builder()
                .document(new byte[10])
                .fechaActuacion("2023-10-01")
                .radicado("8782724792790")
                .build();
        Integer processId = 1;

        Set<ActuacionDocumentResponse> actuacionDocumentResponseSet = Set.of(actuacionDocumentResponse);
        when(storageDataService.downloadAllDocuments(processId)).thenReturn(actuacionDocumentResponseSet);

        assertDoesNotThrow(() -> storageService.downloadAllDocuments(processId));
    }

    @Test
    void downloadAllDocumentsNoSuccessfully() throws ErrorDataServiceException {
        Integer processId = 1;
        when(storageDataService.downloadAllDocuments(processId)).thenReturn(Set.of());

        assertThrows(ErrorDataServiceException.class, () -> storageService.downloadAllDocuments(processId));
    }
}