package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.*;
import com.firma.business.model.Audiencia;
import com.firma.business.payload.request.ProcessDataRequest;
import com.firma.business.payload.response.PageableProcessResponse;
import com.firma.business.payload.response.ProcessAbogadoResponse;
import com.firma.business.intfData.IProcessDataService;
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
public class ProcessDataService implements IProcessDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public Set<Proceso> getProcess() throws ErrorDataServiceException {
        try{
            ResponseEntity<Proceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/get/all", Proceso[].class);
            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String saveProcess(ProcessDataRequest processDataRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ProcessDataRequest> requestEntity = new HttpEntity<>(processDataRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/save",
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
    public PageableProcessResponse getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            String uriRequest = "";
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
            uriRequest = builder.toUriString();
            if (tipoProceso != null) {
                uriRequest+= "&tipoProceso=" + tipoProceso;
            }

            ResponseEntity<PageableProcessResponse> responseEntity = restTemplate.getForEntity(uriRequest, PageableProcessResponse.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public PageableProcessResponse getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            String uriRequest = "";
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
            uriRequest = builder.toUriString();
            if (tipoProceso != null) {
                uriRequest+= "&tipoProceso=" + tipoProceso;
            }

            ResponseEntity<PageableProcessResponse> responseEntity = restTemplate.getForEntity(uriRequest, PageableProcessResponse.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<Proceso> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException {
        try{

            ResponseEntity<Proceso[]> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/process/get/all/estado?name=" + state + "&firmaId=" + firmaId,
                    Proceso[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Proceso getProcessById(Integer processId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get")
                    .queryParam("procesoId", processId);

            ResponseEntity<Proceso> responseEntity = restTemplate.getForEntity(builder.toUriString(), Proceso.class);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String deleteProcess(Integer processId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/delete")
                    .queryParam("processId", processId);

            ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, String.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String updateProcess(Proceso process) throws ErrorDataServiceException {

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Proceso> requestEntity = new HttpEntity<>(process, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/update");

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
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
    public List<Proceso> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException {
        try{

            ResponseEntity<Proceso[]> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/process/get/all/estado/abogado?name=" + name + "&userName=" + userName,
                    Proceso[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public ProcessAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/abogado")
                    .queryParam("procesoId", processId);

            ResponseEntity<ProcessAbogadoResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ProcessAbogadoResponse.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException {
        try{
            ResponseEntity<EstadoProceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/estadoProceso/get/all", EstadoProceso[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException {
        try{

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/audiencia/update?id=" + id + "&enlace=" + enlace,
                    HttpMethod.PUT,
                    null,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String addAudiencia(Audiencia audiencia) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Audiencia> requestEntity = new HttpEntity<>(audiencia, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/audiencia/save",
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
    public Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorDataServiceException {
        try{
            ResponseEntity<Despacho []> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/process/despacho/get/all/notlink?year=" + year,
                    Despacho[].class
            );

            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException {
        try{
            ResponseEntity<TipoProceso[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/tipoProceso/get/all", TipoProceso[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String saveEnlace(Enlace enlaceRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Enlace> requestEntity = new HttpEntity<>(enlaceRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/process/enlace/save",
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
    public TipoProceso findTipoProcesoByNombre(String tipoProceso) throws ErrorDataServiceException {
        try{

            ResponseEntity<TipoProceso> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/process/tipoProceso/get?name=" + tipoProceso,
                    TipoProceso.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Despacho findDespachoByNombre(String despacho) throws ErrorDataServiceException {
        try {

            ResponseEntity<Despacho> responseEntity = restTemplate.getForEntity(
                        apiUrl + "/process/despacho/get?name=" + despacho,
                            Despacho.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public EstadoProceso findEstadoProcesoByNombre(String name) throws ErrorDataServiceException {
        try {

            ResponseEntity<EstadoProceso> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/process/estadoProceso/get?name=" + name,
                    EstadoProceso.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Set<Audiencia> findAllAudienciasByProceso(Integer processId) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/audiencia/get/all")
                    .queryParam("procesoId", processId);

            ResponseEntity<Audiencia[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Audiencia[].class);

            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Despacho findDespachoById(Integer despachoid) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/despacho/get/id")
                    .queryParam("despachoid", despachoid);

            ResponseEntity<Despacho> responseEntity = restTemplate.getForEntity(builder.toUriString(), Despacho.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Proceso findByRadicado(String radicado) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/get/radicado")
                    .queryParam("radicado", radicado);

            ResponseEntity<Proceso> responseEntity = restTemplate.getForEntity(builder.toUriString(), Proceso.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Enlace findByDespachoAndYear(Integer id, String year) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/process/enlace/get")
                    .queryParam("id", id)
                    .queryParam("year", year);

            ResponseEntity<Enlace> responseEntity = restTemplate.getForEntity(builder.toUriString(), Enlace.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Set<DespachoFecha> findAllDespachosWithDateActuacion() throws ErrorDataServiceException {
        try {
            ResponseEntity<DespachoFecha[]> responseEntity = restTemplate.getForEntity(apiUrl + "/process/despacho/get/all/date", DespachoFecha[].class);

            return Set.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }
}
