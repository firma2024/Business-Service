package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.EstadoProceso;
import com.firma.business.service.data.intf.IEstadoProcesoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class EstadoProcesoDataService implements IEstadoProcesoDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException {
        ResponseEntity<EstadoProceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/estadoProceso/all", EstadoProceso[].class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener los estados de proceso");
        }

        return List.of(responseEntity.getBody());
    }
}
