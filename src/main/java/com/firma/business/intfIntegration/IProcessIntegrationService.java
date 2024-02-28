package com.firma.business.intfIntegration;

import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.response.DespachoResponse;
import com.firma.business.payload.request.ProcessRequest;

public interface IProcessIntegrationService {
    ProcessRequest getProcess(String numberProcess) throws ErrorIntegrationServiceException;
    DespachoResponse findUrlDespacho(String nombre, Integer year) throws ErrorIntegrationServiceException;
    ProcessRequest getAllProcess(String numberProcess) throws ErrorIntegrationServiceException;
}
