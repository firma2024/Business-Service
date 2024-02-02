package com.firma.business.controller;

import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.UsuarioResponse;
import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private DataService dataService;

    @GetMapping("/get/abogados")
    public ResponseEntity<?> getFirmaAbogados(@RequestParam Integer firmaId,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "2") Integer size){
        try {
            PageableResponse<UsuarioResponse> response = dataService.getAbogadosByFirma(firmaId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/abogados/filter")
    public ResponseEntity<?> getAbogadosFilter(@RequestParam(required = false) List<String> especialidades,
                                               @RequestParam(defaultValue = "0") Integer numProcesosInicial,
                                               @RequestParam(defaultValue = "5") Integer numProcesosFinal,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "2") Integer size){

        try {
            PageableResponse<UsuarioResponse> response = dataService.getAbogadosByFilter(especialidades, numProcesosInicial, numProcesosFinal, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
