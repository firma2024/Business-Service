package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.data.intf.IProcessDataService;
import com.firma.business.service.integration.intf.IProcessIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProcessService {

    @Autowired
    private IProcessDataService processDataService;
    @Autowired
    private IProcessIntegrationService processIntegrationService;

    public List<ProcesoResponse> getProcess() throws ErrorDataServiceException {
        return processDataService.getProcess();
    }

    public String saveProcess(Proceso process) throws ErrorDataServiceException {
        return processDataService.saveProcess(process);
    }

    public ResponseEntity<?> getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size){
        try {
            return new ResponseEntity<>(processDataService.getProcessByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size){
        try {
            return new ResponseEntity<>(processDataService.getProcessByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getStateProcessesJefe(String state, Integer firmaId){
        try {
            List<ProcesoResponse> processesState = processDataService.getStateProcessesJefe(state, firmaId);
            Map<String, Object> response = Map.of(
                    "value", processesState.size()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getProcessById(Integer processId){
        try {
            return new ResponseEntity<>(processDataService.getProcessById(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteProcess(Integer processId){
        try {
            return new ResponseEntity<>(processDataService.deleteProcess(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateProcess(Proceso process){
        try {
            return new ResponseEntity<>(processDataService.updateProcess(process), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getStateProcessesAbogado(String name, String userName){
        try {
            List<ProcesoResponse> processState = processDataService.getStateProcessesAbogado(name, userName);
            Map<String, Object> response = Map.of(
                    "value", processState.size()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getProcessAbogado(Integer processId){
        try {
            return new ResponseEntity<>(processDataService.getProcessAbogado(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getEstadoProcesos(){
        try {
            return new ResponseEntity<>(processDataService.getEstadoProcesos(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateAudiencia(Integer id, String enlace){
        try {
            return new ResponseEntity<>(processDataService.updateAudiencia(id, enlace), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addAudiencia(AudienciaRequest audiencia){
        try {
            return new ResponseEntity<>(processDataService.addAudiencia(audiencia), HttpStatus.CREATED);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorDataServiceException {
        return processDataService.findAllDespachosWithOutLink(year);
    }

    public ResponseEntity<?> getTipoProcesos(){
        try {
            return new ResponseEntity<>(processDataService.getTipoProcesos(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException {
        return processDataService.saveEnlace(enlaceRequest);
    }

    public ResponseEntity<?> getProcess(String numberProcess){
        try {
            return new ResponseEntity<>(processIntegrationService.getProcess(numberProcess), HttpStatus.OK);
        } catch (ErrorIntegrationServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public DespachoResponse findUrlDespacho(String nombre) throws ErrorIntegrationServiceException {
        return processIntegrationService.findUrlDespacho(nombre);
    }

    public Proceso getAllProcess(String numberProcess) throws ErrorIntegrationServiceException {
        return processIntegrationService.getAllProcess(numberProcess);
    }
}
