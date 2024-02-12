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
        return firmaService.getFirmaByUser(userName);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFirma(@RequestBody FirmaRequest firma) {
        return firmaService.saveFirma(firma);
    }
}
