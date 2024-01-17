package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Despacho;
import com.firma.business.payload.EnlaceRequest;
import com.firma.business.service.data.intf.IEnlaceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnlaceDataService implements IEnlaceDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EnlaceRequest> requestEntity = new HttpEntity<>(enlaceRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/enlace/save",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al guardar el enlace");
        }

        return responseEntity.getBody();
    }
}
