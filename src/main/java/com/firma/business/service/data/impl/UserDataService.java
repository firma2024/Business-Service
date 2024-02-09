package com.firma.business.service.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.data.intf.IUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
public class UserDataService implements IUserDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String saveAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/add/abogado",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException("Error al guardar el abogado");
        }
    }

    @Override
    public String saveJefe(UsuarioRequest userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/add/jefe",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException("Error al guardar el jefe");
        }
    }

    @Override
    public String saveAdmin(UsuarioRequest userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/add/admin",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException("Error al guardar el administrador");
        }
    }

    @Override
    public UsuarioResponse gerInfoJefe(Integer id) throws ErrorDataServiceException {

        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/info/jefe")
                    .queryParam("id", id);

            ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }

    }

    @Override
    public String updateInfoJefe(UsuarioRequest userRequest) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/update/info/jefe")
                    .queryParam("id", userRequest.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

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
    public String updateInfoAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/update/info/abogado",
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
    public String deleteUser(Integer id) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/delete")
                    .queryParam("id", id);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.DELETE,
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
    public UsuarioResponse getUserName(String userName) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/name")
                    .queryParam("userName", userName);

            ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

            return responseEntity.getBody();

        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public List<UsuarioResponse> getAllAbogadosNames(Integer firmaId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/all/names/abogados")
                    .queryParam("firmaId", firmaId);

            ResponseEntity<UsuarioResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public PageableResponse<UsuarioResponse> getAbogadosFilter(Integer numProcesosInicial, Integer numProcesosFinal, List<String> especialidades, Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/jefe/abogados/filter")
                    .queryParam("firmaId", firmaId);
            if (numProcesosInicial != null) {
                builder.queryParam("numProcesosInicial", numProcesosInicial);
            }
            if (numProcesosFinal != null) {
                builder.queryParam("numProcesosFinal", numProcesosFinal);
            }
            if (page != null) {
                builder.queryParam("page", page);
            }
            if (size != null) {
                builder.queryParam("size", size);
            }

            if (especialidades != null) {
                builder.queryParam("especialidades", especialidades);
            }

            ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), PageableResponse.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public UsuarioResponse getAbogado(Integer usuarioId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/abogado")
                    .queryParam("usuarioId", usuarioId);

            ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public List<TipoDocumento> getAllTipoDocumento() throws ErrorDataServiceException {
        try{
            ResponseEntity<TipoDocumento[]> responseEntity = restTemplate.getForEntity(apiUrl + "/user/tipoDocumento/get/all", TipoDocumento[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public Rol getRoleByUser(String userName) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/rol/get/user")
                    .queryParam("username", userName);

            ResponseEntity<Rol> responseEntity = restTemplate.getForEntity(builder.toUriString(), Rol.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public TipoDocumento getTipoDocumento(String name) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/tipoDocumento/get")
                    .queryParam("name", name);

            ResponseEntity<TipoDocumento> responseEntity = restTemplate.getForEntity(builder.toUriString(), TipoDocumento.class);

            return responseEntity.getBody();
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }

    @Override
    public List<TipoAbogado> findAllTipzoAbogado() throws ErrorDataServiceException {
        try{
            ResponseEntity<TipoAbogado[]> responseEntity = restTemplate.getForEntity(apiUrl + "/user/tipoAbogado/get/all", TipoAbogado[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorDataServiceException(e.getMessage());
        }
    }
}
