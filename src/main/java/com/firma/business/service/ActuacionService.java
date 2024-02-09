package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Actuacion;
import com.firma.business.payload.ActuacionEmail;
import com.firma.business.payload.ActuacionRequest;
import com.firma.business.payload.FindProcess;
import com.firma.business.service.data.intf.IActuacionDataService;
import com.firma.business.service.integration.intf.IActuacionIntegrationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Getter
public class ActuacionService {

    @Autowired
    private ProcessService processService;
    @Autowired
    private IActuacionDataService actuacionDataService;
    @Autowired
    private IActuacionIntegrationService actuacionIntegrationService;

    public String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {
        return actuacionDataService.saveActuaciones(actuaciones);
    }

    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {
        return actuacionDataService.findActuacionesNotSend();
    }

    public String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {
        return actuacionDataService.updateActuacionesSend(actuaciones);
    }

    public ResponseEntity<?> getActuacion(Integer id){
        try {
            return new ResponseEntity<>(actuacionDataService.getActuacion(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> getActuacionesFilter(Integer procesoId, String fechaInicioStr, String fechaFinStr, String estadoActuacion, Integer page, Integer size){
        try {
            return new ResponseEntity<>(actuacionDataService.getActuacionesFilter(procesoId, fechaInicioStr, fechaFinStr, estadoActuacion, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> getActuacionesByProcesoAbogado(Integer procesoId, String fechaInicioStr, String fechaFinStr, Boolean existeDoc, Integer page, Integer size){
        try {
            return new ResponseEntity<>(actuacionDataService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public List<ActuacionRequest> findNewActuacion(List <FindProcess> process) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.findNewActuacion(process);
    }

    public List<Integer> sendEmailActuacion(List<ActuacionEmail> actuaciones) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.sendEmailActuacion(actuaciones);
    }
}
