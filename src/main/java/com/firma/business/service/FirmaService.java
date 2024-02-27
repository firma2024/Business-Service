package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.Empleado;
import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.service.data.intf.IFirmaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FirmaService {
    @Autowired
    private IFirmaDataService firmaDataService;

    public Firma getFirmaByUser(String username) throws ErrorDataServiceException {
        return firmaDataService.getFirmaByUser(username);
    }

    public MessageResponse saveFirma(FirmaRequest firma) throws ErrorDataServiceException {
        return new MessageResponse(firmaDataService.saveFirma(firma));
    }

    public Firma findFirmaById(Integer firmaId) throws ErrorDataServiceException {
        return firmaDataService.findFirmaById(firmaId);
    }

    public Empleado findEmpleadoByUsuario(Integer idAbogado) throws ErrorDataServiceException {
        return firmaDataService.findEmpleadoByUsuario(idAbogado);
    }
}
