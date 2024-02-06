package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.EstadoProceso;

import java.util.List;

public interface IEstadoProcesoDataService {
    List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException;
}
