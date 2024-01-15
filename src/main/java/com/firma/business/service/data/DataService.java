package com.firma.business.service.data;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.Despacho;
import com.firma.business.payload.ProcesoResponse;
import com.firma.business.service.data.intf.IActuacionDataService;
import com.firma.business.service.data.intf.IDespachoDataService;
import com.firma.business.service.data.intf.IProcessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DataService {

    @Autowired
    private IProcessDataService processService;
    @Autowired
    private IActuacionDataService actuacionService;
    @Autowired
    private IDespachoDataService despachoService;

    public List<ProcesoResponse> getProcess() throws ErrorDataServiceException {
        return processService.getProcess();
    }

    public String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {
        return actuacionService.saveActuaciones(actuaciones);
    }

    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {
        return actuacionService.findActuacionesNotSend();
    }

    public String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {
        return actuacionService.updateActuacionesSend(actuaciones);
    }

    public Actuacion getActuacionById(Integer id) throws ErrorDataServiceException {
        return actuacionService.getActuacionById(id);
    }

    public Set<Despacho> findAllDespachos() throws ErrorIntegrationServiceException {
        return despachoService.findAllDespachos();
    }

    public String updateDespacho(Despacho despacho) throws ErrorDataServiceException{
        return despachoService.updateDespacho(despacho);
    }
}
