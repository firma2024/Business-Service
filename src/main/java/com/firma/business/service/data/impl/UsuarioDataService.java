package com.firma.business.service.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.Proceso;
import com.firma.business.payload.UsuarioRequest;
import com.firma.business.payload.UsuarioResponse;
import com.firma.business.service.data.intf.IUsuarioDataService;
import com.firma.business.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UsuarioDataService implements IUsuarioDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String uploadPhoto(MultipartFile file, Integer usuarioId) throws ErrorDataServiceException, IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(FileUtils.convertMultiPartToFile(file)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Realizar la solicitud utilizando RestTemplate
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/usuario/upload/photo?usuarioId=" + usuarioId,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al guardar la imagen");
        }

        return responseEntity.getBody();
    }

    @Override
    public byte[] downloadPhoto(Integer usuarioId) throws ErrorDataServiceException {

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                apiUrl + "/usuario/download/photo?usuarioId=" + usuarioId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<byte[]>() {
                }
        );
        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al descargar la foto");
        }
        return responseEntity.getBody();
    }

    @Override
    public PageableResponse<UsuarioResponse> getAbogadosByFirma(Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/get/abogados")
                .queryParam("firmaId", firmaId)
                .queryParam("page", page)
                .queryParam("size", size);

        ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), PageableResponse.class);

        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            throw new ErrorDataServiceException("Error al obtener los abogados");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
    }

    @Override
    public PageableResponse<UsuarioResponse> getAbogadosByFilter(List<String> especialidades, Integer numProcesosInicial, Integer numProcesosFinal, Integer page, Integer size) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "usuario/jefe/abogados/filter")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("numProcesosInicial", numProcesosInicial)
                .queryParam("numProcesosFinal", numProcesosFinal);

        if (especialidades != null) {
            builder.queryParam("especialidades", especialidades);
        }

        ResponseEntity<?> responseEntity = restTemplate.getForEntity(builder.toUriString(), PageableResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener los abogados");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseEntity.getBody(), PageableResponse.class);
    }

    @Override
    public String saveAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/usuario/add/abogado",
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
    public UsuarioResponse getAbogado(Integer abogadoId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/get/abogado")
                .queryParam("usuarioId", abogadoId);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener el abogado");
        }


        return responseEntity.getBody();
    }

    @Override
    public List<UsuarioResponse> getAbogadosNames(Integer firmaId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/get/all/names/abogados")
                .queryParam("firmaId", firmaId);

        ResponseEntity<UsuarioResponse[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse[].class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener los nombres de los abogados");
        }


        return List.of(responseEntity.getBody());
    }

    @Override
    public String deleteUser(Integer userId) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/delete")
                .queryParam("id", userId);

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
    public UsuarioResponse getInfoJefe(Integer id) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/get/info/jefe")
                .queryParam("id", id);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener la información del jefe");
        }

        return responseEntity.getBody();
    }

    @Override
    public String updateInfoJefe(UsuarioRequest userRequest, Integer id) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/update/info/jefe")
                .queryParam("id", id);

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
    public UsuarioResponse getUserByUserName(String userName) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/get/name")
                .queryParam("userName", userName);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener el usuario");
        }
        return responseEntity.getBody();
    }

    @Override
    public UsuarioResponse getInfoAbogado(Integer id) throws ErrorDataServiceException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl + "/usuario/get/info/abogado")
                .queryParam("id", id);

        ResponseEntity<UsuarioResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), UsuarioResponse.class);

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al obtener la información del abogado");
        }
        return responseEntity.getBody();
    }

    @Override
    public String updateInfoAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/usuario/update/info/abogado",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al actualizar la información del abogado");
        }
        return responseEntity.getBody();
    }
}
