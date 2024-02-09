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

@Service
public class UserDataService implements IUserDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String saveAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/user/add/abogado",
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
    public String saveJefe(UsuarioRequest userRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/user/add/jefe",
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
    public String saveAdmin(UsuarioRequest userRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/user/add/admin",
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
    public UsuarioResponse gerInfoJefe(Integer id) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/info/jefe")
                .queryParam("id", id);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener la información del jefe");
        }

        return responseEntity.getBody();
    }

    @Override
    public String updateInfoJefe(UsuarioRequest userRequest) throws ErrorDataServiceException {
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

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al actualizar la información del jefe");
        }

        return responseEntity.getBody();
    }

    @Override
    public String updateInfoAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/user/update/info/abogado",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al actualizar la información del abogado");
        }
        return responseEntity.getBody();
    }

    @Override
    public String deleteUser(Integer id) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/delete")
                .queryParam("id", id);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                null,
                String.class
        );

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al eliminar el usuario");
        }

        return responseEntity.getBody();
    }

    @Override
    public UsuarioResponse getUserName(String userName) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/name")
                .queryParam("userName", userName);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener el usuario");
        }
        return responseEntity.getBody();
    }

    @Override
    public List<UsuarioResponse> getAllAbogadosNames(Integer firmaId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/all/names/abogados")
                .queryParam("firmaId", firmaId);

        ResponseEntity<UsuarioResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse[].class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener los nombres de los abogados");
        }


        return List.of(responseEntity.getBody());
    }

    @Override
    public PageableResponse<UsuarioResponse> getAbogadosFilter(Integer numProcesosInicial, Integer numProcesosFinal, List<String> especialidades, Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException {
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
        System.out.println(builder.toUriString());

        ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), PageableResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener los abogados");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
    }

    @Override
    public UsuarioResponse getAbogado(Integer usuarioId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/abogado")
                .queryParam("usuarioId", usuarioId);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener la información del abogado");
        }
        return responseEntity.getBody();
    }

    @Override
    public List<TipoDocumento> getAllTipoDocumento() throws ErrorDataServiceException {
        ResponseEntity<TipoDocumento[]> responseEntity = restTemplate.getForEntity(apiUrl + "/user/tipoDocumento/get/all", TipoDocumento[].class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener los tipos de documento");
        }

        return List.of(responseEntity.getBody());
    }

    @Override
    public Rol getRoleByUser(String userName) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/rol/get/user")
                .queryParam("username", userName);

        ResponseEntity<Rol> responseEntity = restTemplate.getForEntity(builder.toUriString(), Rol.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener el rol del usuario");
        }
        return responseEntity.getBody();
    }

    @Override
    public TipoDocumento getTipoDocumento(String name) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/tipoDocumento/get")
                .queryParam("name", name);

        ResponseEntity<TipoDocumento> responseEntity = restTemplate.getForEntity(builder.toUriString(), TipoDocumento.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener el tipo de documento");
        }
        return responseEntity.getBody();
    }

    @Override
    public List<TipoAbogado> findAllTipzoAbogado() throws ErrorDataServiceException {
        ResponseEntity<TipoAbogado[]> responseEntity = restTemplate.getForEntity(apiUrl + "/user/tipoAbogado/get/all", TipoAbogado[].class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener los tipos de abogado");
        }

        return List.of(responseEntity.getBody());
    }
}
