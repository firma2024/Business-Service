package com.firma.business.service.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
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
import java.util.Set;

@Service
public class ProcessDataService implements IProcessDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public List<ProcesoResponse> getProcess() throws ErrorDataServiceException {
        try{
            ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/get/all", ProcesoResponse[].class);
            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String saveProcess(Proceso process) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Proceso> requestEntity = new HttpEntity<>(process, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/save",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException("Error al guardar el proceso");
        }
    }

    @Override
    public PageableResponse<Proceso> getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/all/firma/filter")
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

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public PageableResponse<Proceso> getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/all/abogado/filter")
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

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public List<ProcesoResponse> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/all/estado")
                    .queryParam("name", state)
                    .queryParam("firmaId", firmaId);

            ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoResponse[].class);


            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public ProcesoJefeResponse getProcessById(Integer processId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/jefe")
                    .queryParam("procesoId", processId);

            ResponseEntity<ProcesoJefeResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoJefeResponse.class);
            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String deleteProcess(Integer processId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/delete")
                    .queryParam("procesoId", processId);

            ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, String.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String updateProcess(Proceso process) throws ErrorDataServiceException {



        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Proceso> requestEntity = new HttpEntity<>(process, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/update");
            builder.queryParam("procesoId", process.getIdProceso());

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
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
    public List<ProcesoResponse> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/all/estado/abogado")
                    .queryParam("name", name)
                    .queryParam("userName", userName);

            ResponseEntity<ProcesoResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoResponse[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public ProcesoAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/abogado")
                    .queryParam("procesoId", processId);

            ResponseEntity<ProcesoAbogadoResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcesoAbogadoResponse.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException {
        try{
            ResponseEntity<EstadoProceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/estadoProceso/get/all", EstadoProceso[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/process/audiencia/update")
                    .queryParam("id", id)
                    .queryParam("enlace", enlace);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.PUT,
                    null,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String addAudiencia(AudienciaRequest audiencia) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AudienciaRequest> requestEntity = new HttpEntity<>(audiencia, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/audiencia/add",
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
    public Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorDataServiceException {
        try{
            ResponseEntity<Despacho []> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/process/despacho/get/all/notlink?year=" + year,
                    Despacho[].class
            );

            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException {
        try{
            ResponseEntity<TipoProceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/tipoProceso/get/all", TipoProceso[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EnlaceRequest> requestEntity = new HttpEntity<>(enlaceRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/enlace/save",
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
}
