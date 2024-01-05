package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.service.data.intf.IActuacionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class ActuacionDataService implements IActuacionDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl + "/actuacion/save", actuaciones, String.class);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al guardar las actuaciones");
        }
        return responseEntity.getBody();
    }

    @Override
    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {
        ResponseEntity <Actuacion[]> responseEntity = restTemplate.getForEntity(apiUrl + "/actuacion/get/all/send", Actuacion[].class);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener las actuaciones");
        }
        return Set.of(responseEntity.getBody());
    }

    @Override
    public String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(actuaciones, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/actuacion/update/send",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al actualizar las actuaciones");
        }

        return responseEntity.getBody();
    }

    @Override
    public Actuacion getActuacionById(Integer id) throws ErrorDataServiceException {
        ResponseEntity<Actuacion> responseEntity = restTemplate.getForEntity(
                String.format("%s/actuacion/get/?id=%d", apiUrl, id),
                Actuacion.class
        );
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener la actuacion");
        }
        return responseEntity.getBody();
    }
}
