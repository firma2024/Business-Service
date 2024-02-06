package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.TipoProceso;

import java.util.List;

public interface ITipoProcesoDataService {
    List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException;
}
