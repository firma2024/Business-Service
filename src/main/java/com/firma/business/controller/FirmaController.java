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
    public ResponseEntity<?> getInitInfo(@RequestParam String userName){
        try {
            Firma firma = dataService.getFirmaByUser(userName);
            List<ProcesoResponse> activeProcesses = dataService.getStateProcessesJefe( "Activo", firma.getId());
            List<ProcesoResponse> inFavorProcesses = dataService.getStateProcessesJefe("Finalizado a favor", firma.getId());
            List<ProcesoResponse> againstProcesses = dataService.getStateProcessesJefe("Finalizado en contra", firma.getId());
            PageableResponse<?> users = dataService.getAbogadosByFirma(firma.getId(), null, null);

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

    @GetMapping("/get/abogado/init/info")
    public ResponseEntity<?> getInitInfoAbogado(@RequestParam String userName){
        try {
            Firma firma = dataService.getFirmaByUser(userName);
            List<ProcesoResponse> activeProcesses = dataService.getStateProcessesAbogado("Activo", userName);
            List<ProcesoResponse> inFavorProcesses = dataService.getStateProcessesAbogado("Finalizado a favor", userName);
            List<ProcesoResponse> againstProcesses = dataService.getStateProcessesAbogado("Finalizado en contra", userName);

            Map<String, Object> response = Map.of(
                    "firma", firma,
                    "activeProcesses", activeProcesses.size(),
                    "inFavorProcesses", inFavorProcesses.size(),
                    "againstProcesses", againstProcesses.size()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    



}
