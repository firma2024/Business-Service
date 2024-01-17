package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Despacho;
import com.firma.business.service.data.intf.IDespachoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class DespachoDataService implements IDespachoDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorIntegrationServiceException {
        ResponseEntity<Despacho []> responseEntity = restTemplate.getForEntity(
                apiUrl + "/despacho/get/all/notlink?year=" + year,
                Despacho[].class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorIntegrationServiceException("Error al obtener los despachos");
        }

        return Set.of(responseEntity.getBody());
    }

    @Override
    public String updateDespacho(Despacho despacho) throws ErrorDataServiceException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<Despacho> requestEntity = new HttpEntity<>(despacho, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/despacho/update",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al actualizar el despacho");
        }

        return responseEntity.getBody();
    }
}
