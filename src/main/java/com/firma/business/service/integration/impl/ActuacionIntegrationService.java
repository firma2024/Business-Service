package com.firma.business.service.integration.impl;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionEmail;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.FindProcess;
import com.firma.business.service.integration.intf.IActuacionIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class ActuacionIntegrationService implements IActuacionIntegrationService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.integration.url}")
    private String apiUrl;

    @Override
    public List<ActuacionRequest> findNewActuacion(List<FindProcess> process) throws ErrorIntegrationServiceException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<FindProcess>> requestEntity = new HttpEntity<>(process, headers);

        ResponseEntity<ActuacionRequest[]> responseEntity = restTemplate.exchange(
                apiUrl + "find/actuaciones",
                HttpMethod.POST,
                requestEntity,
                ActuacionRequest[].class
        );

        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorIntegrationServiceException("Error al obtener las actuaciones");
        }
        return List.of(responseEntity.getBody());
    }

    @Override
    public List<Integer> sendEmailActuacion(List<ActuacionEmail> actuaciones) throws ErrorIntegrationServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ActuacionEmail>> requestEntity = new HttpEntity<>(actuaciones, headers);

        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(
                apiUrl + "send_email",
                HttpMethod.POST,
                requestEntity,
                Integer[].class
        );

        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorIntegrationServiceException("Error al enviar las actuaciones");
        }

        return List.of(responseEntity.getBody());
    }
}