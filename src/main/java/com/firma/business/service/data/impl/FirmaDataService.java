package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.Empleado;
import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;
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
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/firma/get/user")
                    .queryParam("userName", username);

            ResponseEntity<Firma> responseEntity = restTemplate.getForEntity(builder.toUriString(), Firma.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String saveFirma(FirmaRequest firma) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<FirmaRequest> requestEntity = new HttpEntity<>(firma, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/firma/save",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public Firma findFirmaById(Integer firmaId) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/firma/get")
                    .queryParam("id", firmaId);

            ResponseEntity<Firma> responseEntity = restTemplate.getForEntity(builder.toUriString(), Firma.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public Empleado findEmpleadoByUsuario(Integer idAbogado) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "firma/empleado/get")
                    .queryParam("idAbogado", idAbogado);

            ResponseEntity<Empleado> responseEntity = restTemplate.getForEntity(builder.toUriString(), Empleado.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }
}
