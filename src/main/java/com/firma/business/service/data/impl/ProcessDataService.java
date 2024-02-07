package com.firma.business.service.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.data.intf.IProcessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
public class ProcessDataService implements IProcessDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public List<ProcesoResponse> getProcess() throws ErrorDataServiceException {
        ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(apiUrl + "/proceso/get/all", ProcesoResponse[].class);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener los procesos");
        }
        return List.of(responseEntity.getBody());
    }

    @Override
    public String saveProcess(Proceso process) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Proceso> requestEntity = new HttpEntity<>(process, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/proceso/save",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al guardar el proceso");
        }

        return responseEntity.getBody();
    }

    @Override
    public PageableResponse<Proceso> getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/get/all/firma/filter")
                .queryParam("firmaId", firmaId)
                .queryParam("page", page)
                .queryParam("size", size);

        if (fechaInicioStr != null) {
            builder.queryParam("fechaInicioStr", fechaInicioStr);
        }
        if (fechaFinStr != null) {
            builder.queryParam("fechaFinStr", fechaFinStr);
        }
        if (estadosProceso != null) {
            builder.queryParam("estadosProceso", estadosProceso);
        }
        if (tipoProceso != null) {
            builder.queryParam("tipoProceso", tipoProceso);
        }

        ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), Object.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener los procesos");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
    }

    @Override
    public PageableResponse<Proceso> getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/get/all/abogado/filter")
                .queryParam("abogadoId", abogadoId)
                .queryParam("page", page)
                .queryParam("size", size);

        if (fechaInicioStr != null) {
            builder.queryParam("fechaInicioStr", fechaInicioStr);
        }
        if (fechaFinStr != null) {
            builder.queryParam("fechaFinStr", fechaFinStr);
        }
        if (estadosProceso != null) {
            builder.queryParam("estadosProceso", estadosProceso);
        }
        if (tipoProceso != null) {
            builder.queryParam("tipoProceso", tipoProceso);
        }

        ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), Object.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener los procesos");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
    }

    @Override
    public List<ProcesoResponse> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/get/all/estado")
                .queryParam("name", state)
                .queryParam("firmaId", firmaId);

        ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoResponse[].class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener los procesos");
        }

        return List.of(Objects.requireNonNull(responseEntity.getBody()));

    }

    @Override
    public ProcesoJefeResponse getProcessById(Integer processId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/get/jefe")
                .queryParam("procesoId", processId);

        ResponseEntity<ProcesoJefeResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoJefeResponse.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw  new ErrorDataServiceException("Error al obtener el proceso");
        }

        return responseEntity.getBody();
    }

    @Override
    public String deleteProcess(Integer processId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/delete")
                .queryParam("procesoId", processId);

        ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, String.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al eliminar el proceso");
        }

        return responseEntity.getBody();
    }

    @Override
    public String updateProcess(Proceso process) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Proceso> requestEntity = new HttpEntity<>(process, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/update");
        builder.queryParam("procesoId", process.getIdProceso());

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al actualizar el proceso");
        }

        return responseEntity.getBody();
    }

    @Override
    public List<ProcesoResponse> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/get/all/estado/abogado")
                .queryParam("name", name)
                .queryParam("userName", userName);

        ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoResponse[].class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener los procesos");
        }
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public ProcesoAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/proceso/get/abogado")
                .queryParam("procesoId", processId);

        ResponseEntity<ProcesoAbogadoResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoAbogadoResponse.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener el proceso");
        }
        return responseEntity.getBody();
    }
}
