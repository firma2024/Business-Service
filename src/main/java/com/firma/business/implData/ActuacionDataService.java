package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.intfData.IActuacionDataService;
import com.firma.business.model.EstadoActuacion;
import com.firma.business.model.Actuacion;
import com.firma.business.model.RegistroCorreo;
import com.firma.business.payload.response.PageableActuacionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    public String updateActuacion(Actuacion actuacion) throws ErrorDataServiceException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Actuacion> requestEntity = new HttpEntity<>(actuacion, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/actuacion/update",
                    HttpMethod.PUT,
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
    public String saveRegistroCorreo(RegistroCorreo estadoCorreo) throws ErrorDataServiceException {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl + "/actuacion/registroCorreo/save", estadoCorreo, String.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String saveActuaciones(List<Actuacion> actuaciones) throws ErrorDataServiceException {
        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl + "/actuacion/save/all", actuaciones, String.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {

        try{
            ResponseEntity <Actuacion[]> responseEntity = restTemplate.getForEntity(apiUrl + "/actuacion/get/all/send", Actuacion[].class);

            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
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
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
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
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public PageableActuacionResponse getActuacionesFilter(Integer procesoId, String fechaInicioStr, String fechaFinStr, String estadoActuacion, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            String uriRequest = "";
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
            uriRequest = builder.toUriString();
            if (estadoActuacion != null) {
                uriRequest+= "&estadoActuacion=" + estadoActuacion;
            }

            ResponseEntity<PageableActuacionResponse> responseEntity = restTemplate.getForEntity(
                    uriRequest,
                    PageableActuacionResponse.class);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public PageableActuacionResponse getActuacionesByProcesoAbogado(Integer procesoId, String fechaInicioStr, String fechaFinStr, Boolean existeDoc, Integer page, Integer size) throws ErrorDataServiceException {
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

            ResponseEntity<PageableActuacionResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), PageableActuacionResponse.class);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public EstadoActuacion findEstadoActuacionByName(String state) throws ErrorDataServiceException {
        try {

            ResponseEntity<EstadoActuacion> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/actuacion/estadoActuacion/get?state=" + state,
                    EstadoActuacion.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Actuacion findLastActuacion(Integer processid) throws ErrorDataServiceException {
        try {

            ResponseEntity<Actuacion> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/actuacion/get/last?processid=" + processid,
                    Actuacion.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<Actuacion> findByNoVisto(Integer procesoId) throws ErrorDataServiceException {
        try {

            ResponseEntity<Actuacion[]> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/actuacion/get/byNoVisto?procesoId=" + procesoId,
                    Actuacion[].class);

            return List.of(responseEntity.getBody());
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }
}
