package com.firma.business.controller;

import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadoProceso")
public class EstadoProcesoController {

    @Autowired
    private DataService dataService;


    @GetMapping("/get/all")
    public ResponseEntity<?> getAllEstadoProceso(){
        try {
            return ResponseEntity.ok(dataService.getAllEstadoProceso());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
