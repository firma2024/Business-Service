package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Firma;
import com.firma.business.payload.FirmaRequest;


public interface IFirmaDataService {
    Firma getFirmaByUser(String username) throws ErrorDataServiceException;
    String saveFirma(FirmaRequest firma) throws ErrorDataServiceException;
}
