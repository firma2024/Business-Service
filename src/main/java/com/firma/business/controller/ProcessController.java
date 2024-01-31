package com.firma.business.controller;

import com.firma.business.payload.Proceso;
import com.firma.business.payload.ProcessRequest;
import com.firma.business.service.data.DataService;
import com.firma.business.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

}
