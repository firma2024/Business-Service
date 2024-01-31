package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.EnlaceRequest;
import com.firma.business.payload.Proceso;
import com.firma.business.payload.ProcesoResponse;
import com.firma.business.service.data.intf.IProcessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProcessDataService implements IProcessDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public List<ProcesoResponse> getProcess() throws ErrorDataServiceException {
        ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(apiUrl + "/proceso/get/all", ProcesoResponse[].class);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener los procesos");
        }
        return List.of(responseEntity.getBody());
    }

    @Override
    public String saveProcess(Proceso process) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Proceso> requestEntity = new HttpEntity<>(process, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/proceso/save",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al guardar el proceso");
        }

        return responseEntity.getBody();
    }
}
