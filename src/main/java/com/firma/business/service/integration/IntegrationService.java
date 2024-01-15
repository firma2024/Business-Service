package com.firma.business.service.integration;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.integration.intf.IActuacionIntegrationService;
import com.firma.business.service.integration.intf.IProcessIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class IntegrationService {
    @Autowired
    private IActuacionIntegrationService actuacionIntegrationService;
    @Autowired
    private IProcessIntegrationService processIntegrationService;


    public List<ActuacionRequest> findNewActuacion(List<FindProcess> processFind) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.findNewActuacion(processFind);
    }

    public List<Integer> sendEmailActuacion(List<ActuacionEmail> actuaciones) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.sendEmailActuacion(actuaciones);
    }

    public Proceso getProcess(String numberProcess) throws ErrorIntegrationServiceException {
        return processIntegrationService.getProcess(numberProcess);
    }


    public DespachoResponse findUrlDespacho(String nombre) throws ErrorIntegrationServiceException {
        return processIntegrationService.findUrlDespacho(nombre);
    }
}
