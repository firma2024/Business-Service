package com.firma.business.service.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.PageableResponse;
import com.firma.business.service.data.intf.IActuacionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ActuacionDataService implements IActuacionDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {
        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl + "/actuacion/save", actuaciones, String.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {

        try{
            ResponseEntity <Actuacion[]> responseEntity = restTemplate.getForEntity(apiUrl + "/actuacion/get/all/send", Actuacion[].class);

            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(actuaciones, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/actuacion/update/send",
                    HttpMethod.PUT,
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
    public Actuacion getActuacion(Integer id) throws ErrorDataServiceException {
        try{
            ResponseEntity<Actuacion> responseEntity = restTemplate.getForEntity(
                    String.format("%s/actuacion/get/?id=%d", apiUrl, id),
                    Actuacion.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public PageableResponse<Actuacion> getActuacionesFilter(Integer procesoId, String fechaInicioStr, String fechaFinStr, String estadoActuacion, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/actuacion/jefe/get/all/filter")
                    .queryParam("procesoId", procesoId)
                    .queryParam("page", page)
                    .queryParam("size", size);

            if (fechaInicioStr != null) {
                builder.queryParam("fechaInicioStr", fechaInicioStr);
            }
            if (fechaFinStr != null) {
                builder.queryParam("fechaFinStr", fechaFinStr);
            }
            if (estadoActuacion != null) {
                builder.queryParam("estadoActuacion", estadoActuacion);
            }

            ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), Object.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public PageableResponse<Actuacion> getActuacionesByProcesoAbogado(Integer procesoId, String fechaInicioStr, String fechaFinStr, Boolean existeDoc, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/actuacion/get/all/abogado/filter")
                    .queryParam("procesoId", procesoId)
                    .queryParam("page", page)
                    .queryParam("size", size);

            if (fechaInicioStr != null) {
                builder.queryParam("fechaInicioStr", fechaInicioStr);
            }
            if (fechaFinStr != null) {
                builder.queryParam("fechaFinStr", fechaFinStr);
            }
            if (existeDoc != null) {
                builder.queryParam("existeDoc", existeDoc);
            }

            ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), Object.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }
}
