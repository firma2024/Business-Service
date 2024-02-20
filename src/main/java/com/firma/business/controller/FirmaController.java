package com.firma.business.controller;

import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;
import com.firma.business.service.FirmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/firma")
public class FirmaController {
    @Autowired
    private FirmaService firmaService;

    @Operation(summary = "Obtener firma por username", description = "Obtiene la firma asociada al usuario.")
    @Parameter(name = "userName", description = "Nombre de usuario", required = true)
    @ApiResponse(responseCode = "200", description = "Se retorna la entidad de la firma", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Firma.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener la firma")
    @GetMapping("/get/user")
    public ResponseEntity<?> getFirmaByUser(@RequestParam String userName) {
        try{
            return ResponseEntity.ok(firmaService.getFirmaByUser(userName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Guardar firma", description = "Guarda la firma proporcionada.")
    @ApiResponse(responseCode = "200", description = "Firma guardada correctamente")
    @ApiResponse(responseCode = "400", description = "Error al guardar la firma")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> saveFirma(@RequestBody FirmaRequest firma) {
        try{
            return ResponseEntity.ok(firmaService.saveFirma(firma));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
