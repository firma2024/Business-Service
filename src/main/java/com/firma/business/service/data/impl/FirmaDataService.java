package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Firma;
import com.firma.business.payload.FirmaRequest;
import com.firma.business.service.data.intf.IFirmaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
    public Firma getFirmaByUser(String username) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/firma/get/user")
                .queryParam("userName", username);

        ResponseEntity<Firma> responseEntity = restTemplate.getForEntity(builder.toUriString(), Firma.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener la firma");
        }

        return responseEntity.getBody();
    }

    @Override
    public String saveFirma(FirmaRequest firma) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FirmaRequest> requestEntity = new HttpEntity<>(firma, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/firma/save",
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
