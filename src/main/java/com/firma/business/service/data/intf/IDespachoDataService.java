package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Despacho;

import java.util.Set;

public interface IDespachoDataService {
    Set<Despacho> findAllDespachos() throws ErrorIntegrationServiceException;

    String updateDespacho(Despacho despacho) throws ErrorDataServiceException;
}
