package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Firma;
import com.firma.business.service.data.intf.IFirmaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FirmaDataService implements IFirmaDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public Firma getFirmaById(Integer firmaId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/firma/get/id")
                .queryParam("firmaId", firmaId);

        ResponseEntity<Firma> responseEntity = restTemplate.getForEntity(builder.toUriString(), Firma.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener la firma");
        }

        return responseEntity.getBody();
    }
}
