package com.firma.business.controller;

import com.firma.business.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private IntegrationService integrationService;

    @GetMapping("/get/info")
    public ResponseEntity <?> getInfoProcess(@RequestParam String numberProcess){
        try {
            return ResponseEntity.ok(integrationService.getProcess(numberProcess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
