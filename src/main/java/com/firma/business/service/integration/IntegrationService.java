package com.firma.business.service.integration;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionEmail;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.FindProcess;
import com.firma.business.service.integration.intf.IActuacionIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class IntegrationService {
    @Autowired
    private IActuacionIntegrationService actuacionIntegrationService;


    public List<ActuacionRequest> findNewActuacion(List<FindProcess> processFind) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.findNewActuacion(processFind);
    }

    public List<Integer> sendEmailActuacion(List<ActuacionEmail> actuaciones) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.sendEmailActuacion(actuaciones);
    }
}
