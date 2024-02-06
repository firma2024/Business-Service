package com.firma.business.service.integration.impl;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.DespachoResponse;
import com.firma.business.payload.Proceso;
import com.firma.business.service.integration.intf.IProcessIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessIntegrationService implements IProcessIntegrationService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.integration.url}")
    private String apiUrl;

    @Override
    public Proceso getProcess(String numberProcess) throws ErrorIntegrationServiceException {
        ResponseEntity <Proceso> responseEntity = restTemplate.getForEntity(
                String.format("%sgetProcess/fileNumber=%s", apiUrl, numberProcess),
                Proceso.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorIntegrationServiceException("Error al obtener el proceso");
        }

        return responseEntity.getBody();
    }

    @Override
    public DespachoResponse findUrlDespacho(String nombre) throws ErrorIntegrationServiceException {
        String uri = String.format("%sgetUrl/despacho=%s", apiUrl, nombre);
        ResponseEntity<DespachoResponse> responseEntity = restTemplate.getForEntity(
                uri,
                DespachoResponse.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorIntegrationServiceException("Error al obtener la url del despacho");
        }

        return responseEntity.getBody();
    }

    @Override
    public Proceso getAllProcess(String numberProcess) throws ErrorIntegrationServiceException {
        ResponseEntity <Proceso> responseEntity = restTemplate.getForEntity(
                String.format("%sgetAllProcess/fileNumber=%s", apiUrl, numberProcess),
                Proceso.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorIntegrationServiceException("Error al obtener el proceso");
        }

        return responseEntity.getBody();
    }
}
