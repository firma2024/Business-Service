package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.UsuarioRequest;
import com.firma.business.payload.UsuarioResponse;
import com.firma.business.service.data.intf.IUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private IUserDataService userDataService;

    public ResponseEntity<?> saveAbogado(UsuarioRequest userRequest){
        try {
            return new ResponseEntity<>(userDataService.saveAbogado(userRequest), HttpStatus.CREATED);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> saveJefe(UsuarioRequest userRequest){
        try {
            return new ResponseEntity<>(userDataService.saveJefe(userRequest), HttpStatus.CREATED);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> saveAdmin(UsuarioRequest userRequest){
        try {
            return new ResponseEntity<>(userDataService.saveAdmin(userRequest), HttpStatus.CREATED);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getInfoJefe(Integer id){
        try {
            return new ResponseEntity<>(userDataService.gerInfoJefe(id), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getInfoAbogado(Integer id){
        try {
            return new ResponseEntity<>(userDataService.getAbogado(id), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateInfoAbogado(UsuarioRequest userRequest){
        try {
            return new ResponseEntity<>(userDataService.updateInfoAbogado(userRequest), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateInfoJefe(UsuarioRequest userRequest){
        try {
            return new ResponseEntity<>(userDataService.updateInfoJefe(userRequest), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteUser(Integer id){
        try {
            return new ResponseEntity<>(userDataService.deleteUser(id), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getUserName(String userName){
        try {
            return new ResponseEntity<>(userDataService.getUserName(userName), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllAbogadosNames(Integer firmaId){
        try {
            return new ResponseEntity<>(userDataService.getAllAbogadosNames(firmaId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAbogadosFilter(Integer numProcesosInicial, Integer numProcesosFinal, List<String> especialidades, Integer firmaId, Integer page, Integer size){
        try {
            return new ResponseEntity<>(userDataService.getAbogadosFilter(numProcesosInicial, numProcesosFinal, especialidades, firmaId, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAbogado(Integer usuarioId){
        try {
            return new ResponseEntity<>(userDataService.getAbogado(usuarioId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllTipoDocumento(){
        try {
            return new ResponseEntity<>(userDataService.getAllTipoDocumento(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getTipoDocumento(String name){
        try {
            return new ResponseEntity<>(userDataService.getTipoDocumento(name), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getRoleByUser(String username) {
        try {
            return new ResponseEntity<>(userDataService.getRoleByUser(username), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> findAllTipoAbogado() {
        try {
            return new ResponseEntity<>(userDataService.findAllTipzoAbogado(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getActiveAbogados(Integer firmaId) {
        try {
            PageableResponse<UsuarioResponse> pageableResponse = userDataService.getAbogadosFilter(null, null, null, firmaId, null, null);
            Map<String, Object> response = Map.of("value", pageableResponse.getTotalItems());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
