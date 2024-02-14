package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.response.ActuacionDocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface IStorageDataService {
    String uploadPhoto(MultipartFile file, Integer usuarioId) throws IOException, ErrorDataServiceException;
    byte[] downloadPhoto(Integer usuarioId) throws ErrorDataServiceException;
    String uploadDocument(MultipartFile file, Integer actuacionId) throws ErrorDataServiceException, IOException;
    byte[] downloadDocument(Integer actuacionId) throws ErrorDataServiceException;
    Set<ActuacionDocumentResponse> downloadAllDocuments(Integer procesoId) throws ErrorDataServiceException;
}
