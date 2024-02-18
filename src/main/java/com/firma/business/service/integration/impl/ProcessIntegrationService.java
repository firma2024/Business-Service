package com.firma.business.service.integration.impl;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.Proceso;
import com.firma.business.payload.response.DespachoResponse;
import com.firma.business.payload.request.ProcessRequest;
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
    public ProcessRequest getProcess(String numberProcess) throws ErrorIntegrationServiceException {
        try{
            ResponseEntity <ProcessRequest> responseEntity = restTemplate.getForEntity(
                    String.format("%sgetProcess/fileNumber=%s", apiUrl, numberProcess),
                    ProcessRequest.class
            );
            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorIntegrationServiceException(e.getMessage());
        }
    }

    @Override
    public DespachoResponse findUrlDespacho(String nombre, Integer year) throws ErrorIntegrationServiceException {
        try{
            ResponseEntity<DespachoResponse> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/getUrl/despacho=" + nombre + "/year=" + year,
                    DespachoResponse.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorIntegrationServiceException(e.getMessage());
        }
    }

    @Override
    public ProcessRequest getAllProcess(String numberProcess) throws ErrorIntegrationServiceException {
        try{
            ResponseEntity <ProcessRequest> responseEntity = restTemplate.getForEntity(
                    String.format("%sgetAllProcess/fileNumber=%s", apiUrl, numberProcess),
                    ProcessRequest.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorIntegrationServiceException(e.getMessage());
        }
    }
}
