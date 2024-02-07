package com.firma.business.controller;

import com.firma.business.payload.AudienciaRequest;
import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data/audiencia")
public class AudienciaController {

    @Autowired
    private DataService dataService;

    @PutMapping("/update")
    public ResponseEntity<?> updateAudiencia(@RequestParam Integer id, @RequestParam String enlace){
        try {
            return ResponseEntity.ok(dataService.updateAudiencia(id, enlace));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAudiencia(@RequestBody AudienciaRequest audiencia) {
        try {
            return ResponseEntity.ok(dataService.addAudiencia(audiencia));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
