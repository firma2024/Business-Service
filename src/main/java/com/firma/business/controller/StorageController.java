package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.FileResponse;
import com.firma.business.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return storageService.uploadPhoto(file, usuarioId);
    }

    @GetMapping("/download/photo")
    public ResponseEntity<?> downloadPhoto(@RequestParam Integer usuarioId){
        return storageService.downloadPhoto(usuarioId);
    }

    @PostMapping("/upload/document")
    public ResponseEntity<?> uploadDocument(@RequestParam("document") MultipartFile file, @RequestParam Integer actuacionId){
        return storageService.uploadDocument(file, actuacionId);
    }

    @GetMapping("/download/document")
    public ResponseEntity<?> downloadDocument(@RequestParam Integer actuacionId){
        return storageService.downloadDocument(actuacionId);
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
