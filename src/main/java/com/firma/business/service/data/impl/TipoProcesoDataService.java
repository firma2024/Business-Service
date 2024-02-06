package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.TipoProceso;
import com.firma.business.service.data.intf.ITipoProcesoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class TipoProcesoDataService implements ITipoProcesoDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException {
        ResponseEntity<TipoProceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/tipoProceso/all", TipoProceso[].class);

        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener los tipos de proceso");
        }

        return List.of(responseEntity.getBody());
    }
}
