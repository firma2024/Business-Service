package com.firma.business.controller;

import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipoProceso")
public class TipoProcesoController {

    @Autowired
    private DataService dataService;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllTipoProceso(){
        try {
            return ResponseEntity.ok(dataService.getAllTipoProceso());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
