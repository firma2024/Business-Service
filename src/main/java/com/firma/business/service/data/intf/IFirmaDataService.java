package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.Empleado;
import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;


public interface IFirmaDataService {
    Firma getFirmaByUser(String username) throws ErrorDataServiceException;
    String saveFirma(FirmaRequest firma) throws ErrorDataServiceException;
    Firma findFirmaById(Integer firmaId) throws ErrorDataServiceException;
    Empleado findEmpleadoByUsuario(Integer idAbogado) throws ErrorDataServiceException;
}
