package com.firma.business.controller;

import com.firma.business.payload.UsuarioRequest;
import com.firma.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add/abogado")
    public ResponseEntity<?> addAbogado(@RequestBody UsuarioRequest userRequest) {
        return userService.saveAbogado(userRequest);
    }

    @PostMapping("/add/jefe")
    public ResponseEntity<?> addJefe(@RequestBody UsuarioRequest userRequest) {
        return userService.saveJefe(userRequest);
    }

    @PostMapping("/add/admin")
    public ResponseEntity<?> addAdmin(@RequestBody UsuarioRequest userRequest) {
        return userService.saveAdmin(userRequest);
    }

    @GetMapping("/get/info/jefe")
    public ResponseEntity<?> getPersonalInfo(@RequestParam Integer id) {
        return userService.getInfoJefe(id);
    }

    @GetMapping("/get/info/abogado")
    public ResponseEntity<?> getPersonalInfoAbogado(@RequestParam Integer id) {
        return userService.getInfoAbogado(id);
    }

    @PutMapping("/update/info/abogado")
    public ResponseEntity<?> updatePersonalInfoAbogado(@RequestBody UsuarioRequest userRequest) {
        return userService.updateInfoAbogado(userRequest);
    }

    @PutMapping("/update/info/jefe")
    public ResponseEntity<?> updatePersonalInfoJefe(@RequestBody UsuarioRequest userRequest) {
        return userService.updateInfoJefe(userRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Integer id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/get/name")
    public ResponseEntity<?> getUserName(@RequestParam String name) {
        return userService.getUserName(name);
    }

    @GetMapping("/get/all/names/abogados")
    public ResponseEntity<?> getAllAbogadosNames(@RequestParam Integer firmaId) {
        return userService.getAllAbogadosNames(firmaId);
    }

    @GetMapping("/jefe/abogados/filter")
    public ResponseEntity<?> getAbogadosFilter(@RequestParam(required = false) List<String> especialidades,
                                               @RequestParam Integer firmaId,
                                               @RequestParam(defaultValue = "0") Integer numProcesosInicial,
                                               @RequestParam(defaultValue = "5") Integer numProcesosFinal,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size){
        return userService.getAbogadosFilter(numProcesosInicial, numProcesosFinal, especialidades, firmaId, page, size);
    }

    @GetMapping("/get/abogado")
    public ResponseEntity<?> getAbogado(@RequestParam Integer usuarioId){
        return userService.getAbogado(usuarioId);
    }

    @GetMapping("/get/active/abogados")
    public ResponseEntity<?> getActiveAbogados(@RequestParam Integer firmaId){
        return userService.getActiveAbogados(firmaId);
    }

    @GetMapping("/rol/get/user")
    public ResponseEntity<?> getRoleByUser(@RequestParam String username){
        return userService.getRoleByUser(username);
    }

    @GetMapping("/tipoDocumento/get/all")
    public ResponseEntity<?> getAllTipoDocumento(){
        return userService.getAllTipoDocumento();
    }

    @GetMapping("/tipoAbogado/get/all")
    public ResponseEntity<?> getAllTipoAbogado(){
        return userService.findAllTipoAbogado();
    }
}
