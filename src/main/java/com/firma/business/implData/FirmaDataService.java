package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.intfData.IFirmaDataService;
import com.firma.business.model.Empleado;
import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class FirmaDataService implements IFirmaDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public Firma getFirmaByUser(String username) throws ErrorDataServiceException {
        try{

            ResponseEntity<Firma> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/firma/get/user?userName=" + username,
                    Firma.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
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
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Firma findFirmaById(Integer firmaId) throws ErrorDataServiceException {
        try {
            ResponseEntity<Firma> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/firma/get?id=" + firmaId,
                    Firma.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Empleado findEmpleadoByUsuario(Integer idAbogado) throws ErrorDataServiceException {
        try {

            ResponseEntity<Empleado> responseEntity = restTemplate.getForEntity(
                    apiUrl + "firma/empleado/get?idAbogado=" + idAbogado,
                    Empleado.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }
}
