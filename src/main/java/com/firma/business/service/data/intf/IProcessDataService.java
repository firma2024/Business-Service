package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.ProcesoResponse;

import java.util.List;

public interface IProcessDataService {
    List<ProcesoResponse> getProcess() throws ErrorDataServiceException;
}
