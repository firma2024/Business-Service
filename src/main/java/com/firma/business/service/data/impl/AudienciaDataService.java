package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.AudienciaRequest;
import com.firma.business.payload.Proceso;
import com.firma.business.service.data.intf.IAudienciaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AudienciaDataService implements IAudienciaDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;


    @Override
    public String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/audiencia/update")
                .queryParam("id", id)
                .queryParam("enlace", enlace);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                null,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al actualizar la audiencia");
        }

        return responseEntity.getBody();
    }

    @Override
    public String addAudiencia(AudienciaRequest audiencia) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AudienciaRequest> requestEntity = new HttpEntity<>(audiencia, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/audiencia/add",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al guardar la audiencia");
        }

        return responseEntity.getBody();
    }
}
