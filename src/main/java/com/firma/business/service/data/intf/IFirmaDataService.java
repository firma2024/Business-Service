package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.Firma;

public interface IFirmaDataService {
    Firma getFirmaByUser(String userName) throws ErrorDataServiceException;
}
