package com.firma.business.implIntegration;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.request.ActuacionEmailRequest;
import com.firma.business.payload.request.ActuacionRequest;
import com.firma.business.payload.request.FindProcessRequest;
import com.firma.business.intfIntegration.IActuacionIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class ActuacionIntegrationService implements IActuacionIntegrationService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.integration.url}")
    private String apiUrl;

    @Override
    public List<ActuacionRequest> findNewActuacion(List<FindProcessRequest> process) throws ErrorIntegrationServiceException {

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<FindProcessRequest>> requestEntity = new HttpEntity<>(process, headers);

            ResponseEntity<ActuacionRequest[]> responseEntity = restTemplate.exchange(
                    apiUrl + "find/actuaciones",
                    HttpMethod.POST,
                    requestEntity,
                    ActuacionRequest[].class
            );

            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (Exception e) {
            throw new ErrorIntegrationServiceException(e.getMessage(), 3);
        }
    }

    @Override
    public List<Integer> sendEmailActuacion(List<ActuacionEmailRequest> actuaciones) throws ErrorIntegrationServiceException {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<ActuacionEmailRequest>> requestEntity = new HttpEntity<>(actuaciones, headers);

            ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(
                    apiUrl + "send_email",
                    HttpMethod.POST,
                    requestEntity,
                    Integer[].class
            );

            return List.of(responseEntity.getBody());
        }
        catch (Exception e) {
            throw new ErrorIntegrationServiceException(e.getMessage(), 3);
        }
    }
}
