package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        return processService.getProcess(numberProcess);
    }

    @PostMapping("/save")
    public ResponseEntity <?> addProcess(@RequestBody ProcessRequest processRequest){
        try{
            Proceso process = processService.getAllProcess(processRequest.getNumeroRadicado());
            process.setIdAbogado(processRequest.getIdAbogado());
            process.setIdFirma(processRequest.getIdFirma());

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
        return processService.getProcessByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size);
    }

    @GetMapping("/get/all/abogado/filter")
    public ResponseEntity<?> getProcesosAbogado(@RequestParam Integer abogadoId,
                                                @RequestParam(required = false) String fechaInicioStr,
                                                @RequestParam(required = false) String fechaFinStr,
                                                @RequestParam(required = false) List<String> estadosProceso,
                                                @RequestParam(required = false) String tipoProceso,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size){
        return processService.getProcessByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size);
    }

    @GetMapping("/get/state/processes/jefe")
    public ResponseEntity<?> getAllByEstado(@RequestParam String name, @RequestParam Integer firmaId){
        return processService.getStateProcessesJefe(name, firmaId);
    }


    @GetMapping("/get/jefe")
    public ResponseEntity<?> getJefeProcess(@RequestParam Integer processId){
        return processService.getProcessById(processId);
    }

    @GetMapping("/get/all/estado/abogado")
    public ResponseEntity<?> getAllByEstadoAbogado(@RequestParam String name, @RequestParam String userName){
        return processService.getStateProcessesAbogado(name, userName);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProcess(@RequestParam Integer processId){
        return processService.deleteProcess(processId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProcess(@RequestBody Proceso process){
        return processService.updateProcess(process);
    }

    @GetMapping("/get/abogado")
    public ResponseEntity<?> getProcessAbogado(@RequestParam Integer processId){
        return processService.getProcessAbogado(processId);
    }

    @GetMapping("/estadoProceso/get/all")
    public ResponseEntity<?> getAllEstadoProcesos(){
        return processService.getEstadoProcesos();
    }

    @GetMapping("/tipoProceso/get/all")
    public ResponseEntity<?> getAllTipoProcesos(){
        return processService.getTipoProcesos();
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
        return processService.updateAudiencia(id, enlace);
    }

    @PostMapping("/audiencia/add")
    public ResponseEntity<?> addAudiencia(@RequestBody AudienciaRequest audiencia){
        return processService.addAudiencia(audiencia);
    }

}
