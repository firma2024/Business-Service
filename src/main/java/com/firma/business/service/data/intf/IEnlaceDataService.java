package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.EnlaceRequest;

public interface IEnlaceDataService {
    String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException;
}
