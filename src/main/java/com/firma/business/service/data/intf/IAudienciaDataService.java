package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.AudienciaRequest;

public interface IAudienciaDataService {
    String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException;

    String addAudiencia(AudienciaRequest audiencia) throws ErrorDataServiceException;
}
