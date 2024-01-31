package com.firma.business.service.integration.intf;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.DespachoResponse;
import com.firma.business.payload.Proceso;

public interface IProcessIntegrationService {
    Proceso getProcess(String numberProcess) throws ErrorIntegrationServiceException;

    DespachoResponse findUrlDespacho(String nombre) throws ErrorIntegrationServiceException;

    Proceso getAllProcess(String numberProcess) throws ErrorIntegrationServiceException;
}
