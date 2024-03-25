package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.response.FileResponse;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/business/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Operation(summary = "Subir foto", description = "Sube la foto del usuario dado el id del usuario")
    @Parameter(name = "usuarioId", description = "Id del usuario", required = true)
    @ApiResponse(responseCode = "201", description = "Foto subida correctamente")
    @ApiResponse(responseCode = "400", description = "Error al subir la foto")
    @PostMapping("/upload/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("image") MultipartFile file, @RequestParam Integer usuarioId){
        try {
            return new ResponseEntity<>(storageService.uploadPhoto(file, usuarioId), HttpStatus.CREATED);
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Descargar foto", description = "Descarga la foto del usuario dado el id")
    @Parameter(name = "usuarioId", description = "Id del usuario", required = true)
    @ApiResponse(responseCode = "200", description = "Foto descargada en formato JPEG")
    @ApiResponse(responseCode = "400", description = "Error al descargar la foto")
    @GetMapping("/download/photo")
    public ResponseEntity<?> downloadPhoto(@RequestParam Integer usuarioId){
        try {
            byte [] response = storageService.downloadPhoto(usuarioId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Subir documento de actuacion", description = "Sube el documento de la actuación dado el id de la actuación.")
    @Parameter(name = "actuacionId", description = "Id de la actuación", required = true)
    @ApiResponse(responseCode = "201", description = "Documento subido correctamente")
    @ApiResponse(responseCode = "400", description = "Error al subir el documento")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @PostMapping("/upload/document")
    public ResponseEntity<?> uploadDocument(@RequestParam("document") MultipartFile file, @RequestParam Integer actuacionId){
        try {
            return new ResponseEntity<>(storageService.uploadDocument(file, actuacionId), HttpStatus.CREATED);
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Descargar documento de actuacion", description = "Descarga el documento de la actuación dado el id de la actuación.")
    @Parameter(name = "actuacionId", description = "Id de la actuación", required = true)
    @ApiResponse(responseCode = "200", description = "Documento descargado en formato PDF")
    @ApiResponse(responseCode = "400", description = "Error al descargar el documento")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @GetMapping("/download/document")
    public ResponseEntity<?> downloadDocument(@RequestParam Integer actuacionId){
        try {
            byte [] response = storageService.downloadDocument(actuacionId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Descargar todos los documentos de una actuacion", description = "Descarga todos los documentos de la actuación dado el id de la actuación.")
    @Parameter(name = "procesoId", description = "Id del proceso", required = true)
    @ApiResponse(responseCode = "200", description = "Documentos descargados en un archivo ZIP")
    @ApiResponse(responseCode = "400", description = "Error al descargar los documentos")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @GetMapping("/download/alldocuments")
    public ResponseEntity <?> downloadAllDocuments(@RequestParam Integer procesoId){
        try {
            FileResponse fileResponse = storageService.downloadAllDocuments(procesoId);
            return ResponseEntity.ok()
                    .contentLength(fileResponse.getFile().length)
                    .header("Content-Disposition",  String.format("attachment; filename=\"%s\"", fileResponse.getFileName()))
                    .body(fileResponse.getFile());
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
