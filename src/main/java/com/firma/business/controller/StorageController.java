package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.response.FileResponse;
import com.firma.business.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/business/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("image") MultipartFile file, @RequestParam Integer usuarioId){
        try {
            return new ResponseEntity<>(storageService.uploadPhoto(file, usuarioId), HttpStatus.CREATED);
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/photo")
    public ResponseEntity<?> downloadPhoto(@RequestParam Integer usuarioId){
        try {
            byte [] response = storageService.downloadPhoto(usuarioId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload/document")
    public ResponseEntity<?> uploadDocument(@RequestParam("document") MultipartFile file, @RequestParam Integer actuacionId){
        try {
            return new ResponseEntity<>(storageService.uploadDocument(file, actuacionId), HttpStatus.CREATED);
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/document")
    public ResponseEntity<?> downloadDocument(@RequestParam Integer actuacionId){
        try {
            byte [] response = storageService.downloadDocument(actuacionId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/alldocuments")
    public ResponseEntity <?> downloadAllDocuments(@RequestParam Integer procesoId){
        try {
            FileResponse fileResponse = storageService.downloadAllDocuments(procesoId);
            return ResponseEntity.ok()
                    .contentLength(fileResponse.getFile().length)
                    .header("Content-Disposition",  String.format("attachment; filename=\"%s\"", fileResponse.getFileName()))
                    .body(fileResponse.getFile());
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
