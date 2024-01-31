package com.firma.business.service.data.impl;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionDocumentResponse;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.service.data.intf.IActuacionDataService;
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
import java.util.List;
import java.util.Set;

@Service
public class ActuacionDataService implements IActuacionDataService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.data.url}")
    private String apiUrl;

    @Override
    public String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl + "/actuacion/save", actuaciones, String.class);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al guardar las actuaciones");
        }
        return responseEntity.getBody();
    }

    @Override
    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {
        ResponseEntity <Actuacion[]> responseEntity = restTemplate.getForEntity(apiUrl + "/actuacion/get/all/send", Actuacion[].class);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener las actuaciones");
        }
        return Set.of(responseEntity.getBody());
    }

    @Override
    public String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(actuaciones, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/actuacion/update/send",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al actualizar las actuaciones");
        }

        return responseEntity.getBody();
    }

    @Override
    public Actuacion getActuacionById(Integer id) throws ErrorDataServiceException {
        ResponseEntity<Actuacion> responseEntity = restTemplate.getForEntity(
                String.format("%s/actuacion/get/?id=%d", apiUrl, id),
                Actuacion.class
        );
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ErrorDataServiceException("Error al obtener la actuacion");
        }
        return responseEntity.getBody();
    }

    @Override
    public String uploadDocument(MultipartFile file, Integer actuacionId) throws IOException, ErrorDataServiceException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("doc", new FileSystemResource(FileUtils.convertMultiPartToFile(file)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Realizar la solicitud utilizando RestTemplate
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/actuacion/upload/document?actuacionId=" + actuacionId,
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
                apiUrl + "/actuacion/download/document?actuacionId=" + actuacionId,
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
        String url = apiUrl + "actuacion/download/all/documents?procesoId=" + procesoId;
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
