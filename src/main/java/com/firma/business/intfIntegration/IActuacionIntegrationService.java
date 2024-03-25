package com.firma.business.intfIntegration;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.request.ActuacionEmailRequest;
import com.firma.business.payload.request.ActuacionRequest;
import com.firma.business.payload.request.FindProcessRequest;

import java.util.List;

public interface IActuacionIntegrationService {
    List<ActuacionRequest> findNewActuacion(List <FindProcessRequest> process) throws ErrorIntegrationServiceException;
    List<Integer> sendEmailActuacion(List<ActuacionEmailRequest> actuaciones) throws ErrorIntegrationServiceException;
}
