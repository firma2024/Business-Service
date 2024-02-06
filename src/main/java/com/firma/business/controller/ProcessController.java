package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.Proceso;
import com.firma.business.payload.ProcesoResponse;
import com.firma.business.payload.ProcessRequest;
import com.firma.business.service.data.DataService;
import com.firma.business.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private DataService dataService;

    @GetMapping("/get/info")
    public ResponseEntity <?> getInfoProcess(@RequestParam String numberProcess){
        try {
            return ResponseEntity.ok(integrationService.getProcess(numberProcess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity <?> addProcess(@RequestBody ProcessRequest processRequest){
        try {

            Proceso process = integrationService.getAllProcess(processRequest.getNumeroRadicado());
            process.setIdAbogado(processRequest.getIdAbogado());
            process.setIdFirma(processRequest.getIdFirma());

            String response = dataService.saveProcess(process);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
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
                                                      @RequestParam(defaultValue = "2") Integer size){

        try {

            PageableResponse<Proceso> response = dataService.getProcessByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size);
            return ResponseEntity.ok(response);

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
                                                @RequestParam(defaultValue = "2") Integer size){

        try {

            PageableResponse<Proceso> response = dataService.getProcessByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size);
            return ResponseEntity.ok(response);

        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/jefe")
    public ResponseEntity<?> getJefeProcess(@RequestParam Integer processId){
        try {
            return ResponseEntity.ok(dataService.getProcessById(processId));
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProcess(@RequestParam Integer processId){
        try {
            return ResponseEntity.ok(dataService.deleteProcess(processId));
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProcess(@RequestBody Proceso process){
        try {
            return ResponseEntity.ok(dataService.updateProcess(process));
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
