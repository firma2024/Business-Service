package com.firma.business.service;

import com.firma.business.payload.FirmaRequest;
import com.firma.business.service.data.intf.IFirmaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class FirmaService {
    @Autowired
    private IFirmaDataService firmaDataService;

    public ResponseEntity<?> getFirmaByUser(String username) {
        try {
            return ResponseEntity.ok(firmaDataService.getFirmaByUser(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> saveFirma(FirmaRequest firma) {
        try {
            return ResponseEntity.ok(firmaDataService.saveFirma(firma));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
