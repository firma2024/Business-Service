package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.Rol;
import com.firma.business.model.TipoAbogado;
import com.firma.business.model.TipoDocumento;
import com.firma.business.model.Usuario;
import com.firma.business.payload.request.UserDataRequest;
import com.firma.business.payload.request.UserRequest;
import com.firma.business.payload.response.PageableUserResponse;
import com.firma.business.intfData.IUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    public String saveUser(UserDataRequest userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserDataRequest> requestEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/save",
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
    public String updateUser(Usuario userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> requestEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/update",
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
    public Integer getNumberAssignedProcesses(Integer id) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/assigned/processes")
                    .queryParam("id", id);

            ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(builder.toUriString(), Integer.class);

            return responseEntity.getBody();
        }catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
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
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }
    @Override
    public Usuario findUserByUserName(String userName) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/name")
                    .queryParam("userName", userName);

            ResponseEntity<Usuario> responseEntity = restTemplate.getForEntity(builder.toUriString(), Usuario.class);

            return responseEntity.getBody();

        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<Usuario> findAllAbogadosNames(Integer firmaId) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get/all/names/abogados")
                    .queryParam("firmaId", firmaId);

            ResponseEntity<Usuario[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Usuario[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public String checkInsertUser(UserRequest userRequest) throws ErrorDataServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl + "/user/check/insert",
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
    public PageableUserResponse getAbogadosByFirmaFilter(List<String> especialidades, Integer firmaId, Integer roleId, Integer page, Integer size) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/jefe/abogados/filter")
                    .queryParam("firmaId", firmaId)
                    .queryParam("roleId", roleId);
            if (page != null) {
                builder.queryParam("page", page);
            }
            if (size != null) {
                builder.queryParam("size", size);
            }

            if (especialidades != null) {
                builder.queryParam("especialidades", especialidades);
            }

            ResponseEntity<PageableUserResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), PageableUserResponse.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<TipoDocumento> getAllTipoDocumento() throws ErrorDataServiceException {
        try{
            ResponseEntity<TipoDocumento[]> responseEntity = restTemplate.getForEntity(apiUrl + "/user/tipoDocumento/get/all", TipoDocumento[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Rol getRoleByUser(String userName) throws ErrorDataServiceException {
        try{
            ResponseEntity<Rol> responseEntity = restTemplate.getForEntity(apiUrl + "/user/rol/get/user?username=" +userName, Rol.class);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public List<TipoAbogado> findAllTipoAbogado() throws ErrorDataServiceException {
        try{
            ResponseEntity<TipoAbogado[]> responseEntity = restTemplate.getForEntity(apiUrl + "/user/tipoAbogado/get/all", TipoAbogado[].class);

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public TipoDocumento findTipoDocumendoByName(String tipoDocumento) throws ErrorDataServiceException {
        try{

            ResponseEntity<TipoDocumento> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/user/tipoDocumento/get?name=" + tipoDocumento,
                    TipoDocumento.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Rol findRolByName(String name) throws ErrorDataServiceException {
        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/rol/get")
                    .queryParam("roleName", name);

            ResponseEntity<Rol> responseEntity = restTemplate.getForEntity(builder.toUriString(), Rol.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public TipoAbogado findTipoAbogadoByName(String specialty) throws ErrorDataServiceException{
        try {

            ResponseEntity<TipoAbogado> responseEntity = restTemplate.getForEntity(
                    apiUrl + "/user/tipoAbogado/get?name="+specialty,
                    TipoAbogado.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    @Override
    public Usuario findUserById(Integer id) throws ErrorDataServiceException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/user/get")
                    .queryParam("id", id);

            ResponseEntity<Usuario> responseEntity = restTemplate.getForEntity(builder.toUriString(), Usuario.class);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            throw new ErrorDataServiceException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }
}
