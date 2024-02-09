package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.ActuacionDocumentResponse;
import com.firma.business.service.data.intf.IStorageDataService;
import com.firma.business.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
public class StorageDataService implements IStorageDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String uploadPhoto(MultipartFile file, Integer usuarioId) throws IOException, ErrorDataServiceException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(FileUtils.convertMultiPartToFile(file)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/storage/upload/photo?usuarioId=" + usuarioId,
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
                apiUrl + "/storage/download/photo?usuarioId=" + usuarioId,
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
    public String uploadDocument(MultipartFile file, Integer actuacionId) throws ErrorDataServiceException, IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("doc", new FileSystemResource(FileUtils.convertMultiPartToFile(file)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/storage/upload/document?actuacionId=" + actuacionId,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()){
            throw new ErrorDataServiceException("Error al guardar el documento");
        }

        return responseEntity.getBody();
    }

    @Override
    public byte[] downloadDocument(Integer actuacionId) throws ErrorDataServiceException {
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                apiUrl + "/storage/download/document?actuacionId=" + actuacionId,
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
    public Set<ActuacionDocumentResponse> downloadAllDocuments(Integer procesoId) throws ErrorDataServiceException {
        String url = apiUrl + "storage/download/all/documents?procesoId=" + procesoId;
        ResponseEntity <ActuacionDocumentResponse[]> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ActuacionDocumentResponse[].class
        );

        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener los documentos");
        }
        return Set.of(responseEntity.getBody());
    }
}