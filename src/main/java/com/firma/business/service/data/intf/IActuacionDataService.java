package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionDocumentResponse;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.ProcesoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IActuacionDataService {
    String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException;

    Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException;
    String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException;

    Actuacion getActuacionById(Integer id) throws ErrorDataServiceException;

    String uploadDocument(MultipartFile file, Integer actuacionId) throws IOException, ErrorDataServiceException;

    byte[] downloadDocument(Integer actuacionId) throws ErrorDataServiceException;

    Set<ActuacionDocumentResponse> downloadAllDocuments(Integer procesoId) throws ErrorDataServiceException;
}
