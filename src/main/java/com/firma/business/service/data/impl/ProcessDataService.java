package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.ProcesoResponse;
import com.firma.business.service.data.intf.IProcessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
}
