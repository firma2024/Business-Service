package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.FileResponse;
import com.firma.business.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private DataService dataService;


    @PostMapping("/upload/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("image") MultipartFile file, @RequestParam Integer usuarioId){
        try {
            String response = dataService.uploadPhoto(file, usuarioId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/download/photo")
    public ResponseEntity<?> downloadPhoto(@RequestParam Integer usuarioId){
        try {
            byte[] response = dataService.downloadPhoto(usuarioId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/upload/document")
    public ResponseEntity<?> uploadDocument(@RequestParam("document") MultipartFile file, @RequestParam Integer actuacionId){
        try {
            String response = dataService.uploadDocument(file, actuacionId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/download/document")
    public ResponseEntity<?> downloadDocument(@RequestParam Integer actuacionId){
        try {
            byte[] response = dataService.downloadDocument(actuacionId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/download/alldocuments")
    public ResponseEntity <?> downloadAllDocuments(@RequestParam Integer procesoId){
        try {
            FileResponse fileResponse = dataService.downloadAllDocuments(procesoId);
            return ResponseEntity.ok()
                    .contentLength(fileResponse.getFile().length)
                    .header("Content-Disposition",  String.format("attachment; filename=\"%s\"", fileResponse.getFileName()))
                    .body(fileResponse.getFile());
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>("Error to download documents", HttpStatus.BAD_REQUEST);
        }
    }
}
