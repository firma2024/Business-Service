package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.Despacho;
import com.firma.business.model.Proceso;
import com.firma.business.payload.request.*;
import com.firma.business.payload.response.DespachoResponse;
import com.firma.business.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/business/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;
    private Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @GetMapping("/get/info")
    public ResponseEntity<?> getInfoProcess(@RequestParam String numberProcess){
        try {
            return new ResponseEntity<>(processService.getProcess(numberProcess), HttpStatus.OK);
        } catch (ErrorIntegrationServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity <?> addProcess(@RequestBody ProcessBusinessRequest processRequest){
        try{
            processService.findByRadicado(processRequest.getNumeroRadicado());
            ProcessRequest process = processService.getAllProcess(processRequest.getNumeroRadicado());
            process.setIdAbogado(processRequest.getIdAbogado());

            return ResponseEntity.ok(processService.saveProcess(process));

        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/all/filter")
    public ResponseEntity<?> getProcesosByFirmaFilter(@RequestParam(required = false) String fechaInicioStr,
                                                      @RequestParam Integer firmaId,
                                                      @RequestParam(required = false) String fechaFinStr,
                                                      @RequestParam(required = false) List<String> estadosProceso,
                                                      @RequestParam(required = false) String tipoProceso,
                                                      @RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size){
        try {
            return new ResponseEntity<>(processService.getProcessesByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/all/abogado/filter")
    public ResponseEntity<?> getProcesosAbogado(@RequestParam Integer abogadoId,
                                                @RequestParam(required = false) String fechaInicioStr,
                                                @RequestParam(required = false) String fechaFinStr,
                                                @RequestParam(required = false) List<String> estadosProceso,
                                                @RequestParam(required = false) String tipoProceso,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size){
        try {
            return new ResponseEntity<>(processService.getProcessesByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/state/processes/jefe")
    public ResponseEntity<?> getAllByEstado(@RequestParam String name, @RequestParam Integer firmaId){
        try {
            return new ResponseEntity<>(processService.getStateProcessesJefe(name, firmaId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/state/processes/abogado")
    public ResponseEntity<?> getAllByEstadoAbogado(@RequestParam String name, @RequestParam String userName){
        try{
            return new ResponseEntity<>(processService.getStateProcessesAbogado(name, userName), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/jefe")
    public ResponseEntity<?> getJefeProcess(@RequestParam Integer processId){
        try {
            return new ResponseEntity<>(processService.getJefeProcess(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProcess(@RequestParam Integer processId){
        try {
            return new ResponseEntity<>(processService.deleteProcess(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProcess(@RequestBody ProcessUpdateRequest process){
        try {
            return new ResponseEntity<>(processService.updateProcess(process), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/abogado")
    public ResponseEntity<?> getProcessAbogado(@RequestParam Integer processId){
        try {
            return new ResponseEntity<>(processService.getProcessAbogado(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estadoProceso/get/all")
    public ResponseEntity<?> getAllEstadoProcesos(){
        try {
            return new ResponseEntity<>(processService.getEstadoProcesos(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tipoProceso/get/all")
    public ResponseEntity<?> getAllTipoProcesos(){
        try {
            return new ResponseEntity<>(processService.getTipoProcesos(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 600000)
    public void updateDespacho(){
        try {
            logger.info("Buscando enlaces de despachos");
            Integer year = LocalDate.now().getYear();
            Set<Despacho> despachos = processService.findAllDespachosWithOutLink(year);

            for (Despacho despacho : despachos) {
                logger.info(despacho.getNombre());
                DespachoResponse url = processService.findUrlDespacho(despacho.getNombre());
                EnlaceRequest en = EnlaceRequest.builder()
                        .url(url.getUrl_despacho())
                        .despachoid(despacho.getId())
                        .fechaconsulta(LocalDate.now())
                        .build();
                logger.info(processService.saveEnlace(en));
            }

        } catch (ErrorIntegrationServiceException | ErrorDataServiceException e) {
            logger.error(e.getMessage());
        }
    }

    @PutMapping("/audiencia/update")
    public ResponseEntity<?> updateAudiencia(@RequestParam Integer id, @RequestParam String enlace){
        try {
            return new ResponseEntity<>(processService.updateAudiencia(id, enlace), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/audiencia/add")
    public ResponseEntity<?> addAudiencia(@RequestBody AudienciaRequest audiencia){
        try {
            return new ResponseEntity<>(processService.addAudiencia(audiencia), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
