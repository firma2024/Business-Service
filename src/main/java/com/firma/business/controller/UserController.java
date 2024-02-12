package com.firma.business.controller;

import com.firma.business.payload.request.UserAbogadoUpdateRequest;
import com.firma.business.payload.request.UserJefeUpdateRequest;
import com.firma.business.payload.request.UserRequest;
import com.firma.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add/abogado")
    public ResponseEntity<?> addAbogado(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.saveAbogado(userRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add/jefe")
    public ResponseEntity<?> addJefe(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.saveJefe(userRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add/admin")
    public ResponseEntity<?> addAdmin(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.saveAdmin(userRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/info/jefe")
    public ResponseEntity<?> getPersonalInfo(@RequestParam String userName) {
        try {
            return new ResponseEntity<>(userService.getInfoJefe(userName), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/info/abogado")
    public ResponseEntity<?> updatePersonalInfoAbogado(@RequestBody UserAbogadoUpdateRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.updateInfoAbogado(userRequest), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/info/jefe")
    public ResponseEntity<?> updatePersonalInfoJefe(@RequestBody UserJefeUpdateRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.updateInfoJefe(userRequest), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Integer id) {
        try {
            return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/name")
    public ResponseEntity<?> getUserName(@RequestParam String name) {
        try {
            return new ResponseEntity<>(userService.getUserName(name), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/all/names/abogados")
    public ResponseEntity<?> getAllAbogadosNames(@RequestParam Integer firmaId) {
        try {
            return new ResponseEntity<>(userService.getAllAbogadosNames(firmaId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/jefe/abogados/filter")
    public ResponseEntity<?> getAbogadosFilter(@RequestParam(required = false) List<String> especialidades,
                                               @RequestParam Integer firmaId,
                                               @RequestParam(defaultValue = "0") Integer numProcesosInicial,
                                               @RequestParam(defaultValue = "5") Integer numProcesosFinal,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size){
        try {
            return new ResponseEntity<>(userService.getAbogadosByFirmaFilter(numProcesosInicial, numProcesosFinal, especialidades, firmaId, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/abogado")
    public ResponseEntity<?> getAbogado(@RequestParam String userName){
        try {
            return new ResponseEntity<>(userService.getInfoAbogado(userName), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/active/abogados")
    public ResponseEntity<?> getActiveAbogados(@RequestParam Integer firmaId){
        try {
            return new ResponseEntity<>(userService.getActiveAbogados(firmaId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/rol/get/user")
    public ResponseEntity<?> getRoleByUser(@RequestParam String username){
        try {
            return new ResponseEntity<>(userService.getRoleByUser(username), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tipoDocumento/get/all")
    public ResponseEntity<?> getAllTipoDocumento(){
        try {
            return new ResponseEntity<>(userService.getAllTipoDocumento(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tipoAbogado/get/all")
    public ResponseEntity<?> getAllTipoAbogado(){
        try {
            return new ResponseEntity<>(userService.findAllTipoAbogado(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
