package com.firma.business.service.integration.intf;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionEmail;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.FindProcess;

import java.util.List;
import java.util.Set;

public interface IActuacionIntegrationService {
    List<ActuacionRequest> findNewActuacion(List <FindProcess> process) throws ErrorIntegrationServiceException;
    List<Integer> sendEmailActuacion(List<ActuacionEmail> actuaciones) throws ErrorIntegrationServiceException;
}
