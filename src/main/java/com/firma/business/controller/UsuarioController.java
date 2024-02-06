package com.firma.business.controller;

import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.UsuarioRequest;
import com.firma.business.payload.UsuarioResponse;
import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private DataService dataService;

    @GetMapping("/get/all/abogados")
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

    @PostMapping("/add/abogado")
    public ResponseEntity<?> addAbogado(@RequestBody UsuarioRequest userRequest){
        try {
            String response = dataService.saveAbogado(userRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/abogado")
    public ResponseEntity<?> getAbogado(@RequestParam Integer userId){
        try {
            UsuarioResponse response = dataService.getAbogado(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/all/names/abogados")
    public ResponseEntity<?> getAbogadosNames(@RequestParam Integer firmaId){
        try {
            List<UsuarioResponse> response = dataService.getAbogadosNames(firmaId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAbogado(@RequestParam Integer userId){
        try {
            String response = dataService.deleteUser(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/info/jefe")
    public ResponseEntity <?> getPersonalInfo (@RequestParam Integer id){
        try {
            UsuarioResponse response = dataService.getInfoJefe(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/info/jefe")
    public ResponseEntity <?> updatePersonalInfo (@RequestBody UsuarioRequest userRequest, @RequestParam Integer id){
        try {
            String response = dataService.updateInfoJefe(userRequest, id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
