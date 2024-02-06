package com.firma.business.controller;

import com.firma.business.payload.Firma;
import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.ProcesoResponse;
import com.firma.business.payload.UsuarioResponse;
import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/firma")
public class FirmaController {

    @Autowired
    private DataService dataService;

    @GetMapping("/get/jefe/init/info")
    public ResponseEntity<?> getInitInfo(@RequestParam Integer firmaId){
        try {
            Firma firma = dataService.getFirmaById(firmaId);
            List<ProcesoResponse> activeProcesses = dataService.getStateProcesses( "Activo",firmaId);
            List<ProcesoResponse> inFavorProcesses = dataService.getStateProcesses("Finalizado a favor", firmaId);
            List<ProcesoResponse> againstProcesses = dataService.getStateProcesses("Finalizado en contra", firmaId);
            PageableResponse<?> users = dataService.getAbogadosByFirma(firmaId, null, null);

            Map<String, Object> response = Map.of(
                    "firma", firma,
                    "activeProcesses", activeProcesses.size(),
                    "inFavorProcesses", inFavorProcesses.size(),
                    "againstProcesses", againstProcesses.size(),
                    "abogados", users.getTotalItems()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
