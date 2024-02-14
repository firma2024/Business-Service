package com.firma.business.controller;

import com.firma.business.payload.request.FirmaRequest;
import com.firma.business.service.FirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/firma")
public class FirmaController {
    @Autowired
    private FirmaService firmaService;

    @GetMapping("/get/user")
    public ResponseEntity<?> getFirmaByUser(@RequestParam String userName) {
        try{
            return ResponseEntity.ok(firmaService.getFirmaByUser(userName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFirma(@RequestBody FirmaRequest firma) {
        try{
            return ResponseEntity.ok(firmaService.saveFirma(firma));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
