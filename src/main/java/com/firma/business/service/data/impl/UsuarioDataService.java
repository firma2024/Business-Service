package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
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
import java.nio.file.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
}
