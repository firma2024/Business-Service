package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.EstadoProceso;
import com.firma.business.model.TipoProceso;
import com.firma.business.payload.request.AudienciaRequest;
import com.firma.business.payload.request.ProcessBusinessRequest;
import com.firma.business.payload.request.ProcessRequest;
import com.firma.business.payload.request.ProcessUpdateRequest;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.response.ProcessAbogadoResponse;
import com.firma.business.payload.response.ProcessJefeResponse;
import com.firma.business.service.ProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Operation(summary = "Obtener informacion inicial del proceso por número de radicado", description = "Obtiene el proceso asociado al número de radicado consultando la informacion a Integration-service")
    @Parameter(name = "numberProcess", description = "Número de radicado del proceso", required = true)
    @ApiResponse(responseCode = "200", description = "Informacion inicial del proceso exeptuando los campos actuaciones, IdAbogado y idFirma", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProcessRequest.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener la informacion del proceso")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @GetMapping("/get/info")
    public ResponseEntity<?> getInfoProcess(@RequestParam String numberProcess){
        try {
            return new ResponseEntity<>(processService.getProcess(numberProcess), HttpStatus.OK);
        } catch (ErrorIntegrationServiceException e) {
            if (e.getStatusCode() == 404) {
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
            }
            if (e.getStatusCode() == 503){
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
            }
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Guardar proceso", description = "Guarda el proceso en la base de datos")
    @ApiResponse(responseCode = "200", description = "Proceso guardado correctamente", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    @ApiResponse(responseCode = "400", description = "Error al guardar el proceso")
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    public ResponseEntity <?> addProcess(@RequestBody ProcessBusinessRequest processRequest){
        try{
            processService.findByRadicado(processRequest.getNumeroRadicado());
            ProcessRequest process = processService.getAllProcess(processRequest.getNumeroRadicado());
            process.setIdAbogado(processRequest.getIdAbogado());

            return new ResponseEntity<>(processService.saveProcess(process), HttpStatus.CREATED);

        }catch (ErrorIntegrationServiceException e) {
            if (e.getStatusCode() == 404) {
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
            }
            if (e.getStatusCode() == 503){
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
            }
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ErrorDataServiceException e) {
            if (e.getStatusCode() == 409){
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Filtrar procesos por firma para el jefe", description = "Filtra los procesos por firma para el jefe, en caso de no adjuntar nungun filtro lista todos los procesos")
    @Parameter(name = "fechaInicioStr", description = "Fecha de inicio del filtro", required = false)
    @Parameter(name = "firmaId", description = "Id de la firma", required = true)
    @Parameter(name = "fechaFinStr", description = "Fecha de fin del filtro", required = false)
    @Parameter(name = "estadosProceso", description = "Estados del proceso", required = false)
    @Parameter(name = "tipoProceso", description = "Tipo de proceso", required = false)
    @Parameter(name = "page", description = "Número de página default 0", required = false)
    @Parameter(name = "size", description = "Tamaño de la página default 10", required = false)
    @ApiResponse(responseCode = "200", description = "Procesos filtrados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageableResponse.class))})
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @GetMapping("/get/all/filter")
    public ResponseEntity<?> getProcesosByFirmaFilter(@RequestParam(required = false) String fechaInicioStr,
                                                      @RequestParam Integer firmaId,
                                                      @RequestParam(required = false) String fechaFinStr,
                                                      @RequestParam(required = false) List<String> estadosProceso,
                                                      @RequestParam(required = false) String tipoProceso,
                                                      @RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size){
        try {
            return new ResponseEntity<>(processService.getProcessesByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Filtrar procesos de un abogado", description = "Filtra los procesos de un abogado, en caso de no adjuntar nungun filtro lista todos los procesos")
    @Parameter(name = "abogadoId", description = "Id del abogado", required = true)
    @Parameter(name = "fechaInicioStr", description = "Fecha de inicio de radicacion", required = false)
    @Parameter(name = "fechaFinStr", description = "Fecha de fin de radicacion", required = false)
    @Parameter(name = "estadosProceso", description = "Estados del proceso", required = false)
    @Parameter(name = "tipoProceso", description = "Tipo de proceso", required = false)
    @Parameter(name = "page", description = "Número de página default 0", required = false)
    @Parameter(name = "size", description = "Tamaño de la página default 10", required = false)
    @ApiResponse(responseCode = "200", description = "Entidad paginada", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageableResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los procesos")
    @GetMapping("/get/all/abogado/filter")
    public ResponseEntity<?> getProcesosAbogado(@RequestParam Integer abogadoId,
                                                @RequestParam(required = false) String fechaInicioStr,
                                                @RequestParam(required = false) String fechaFinStr,
                                                @RequestParam(required = false) List<String> estadosProceso,
                                                @RequestParam(required = false) String tipoProceso,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size){
        try {
            return new ResponseEntity<>(processService.getProcessesByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtiene el numero de procesos por firma y estado", description = "Obtiene el numero de procesos por firma para el jefe de abogados")
    @Parameter(name = "firmaId", description = "Id de la firma", required = true)
    @Parameter(name = "name", description = "Nombre del estado (Activo, Finalzado a favor, Finalizado en contra", required = true)
    @ApiResponse(responseCode = "200", description = "Numero de procesos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener el numero de procesos")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @GetMapping("/get/state/processes/jefe")
    public ResponseEntity<?> getAllByEstado(@RequestParam String name, @RequestParam Integer firmaId){
        try {
            return new ResponseEntity<>(processService.getStateProcessesJefe(name, firmaId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtiene el numero de procesos por abogado y estado", description = "Obtiene el numero de procesos por abogado")
    @Parameter(name = "name", description = "Nombre del estado (Activo, Finalzado a favor, Finalizado en contra", required = true)
    @Parameter(name = "userName", description = "Nombre de usuario del abogado", required = true)
    @ApiResponse(responseCode = "200", description = "Numero de procesos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener el numero de procesos")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @GetMapping("/get/state/processes/abogado")
    public ResponseEntity<?> getAllByEstadoAbogado(@RequestParam String name, @RequestParam String userName){
        try{
            return new ResponseEntity<>(processService.getStateProcessesAbogado(name, userName), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtiene el proceso por id jefe", description = "Obtiene la informacion del proceso por id para el jefe de abogados")
    @Parameter(name = "processId", description = "Id del proceso", required = true)
    @ApiResponse(responseCode = "200", description = "Proceso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProcessJefeResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener el proceso")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @GetMapping("/get/jefe")
    public ResponseEntity<?> getJefeProcess(@RequestParam Integer processId){
        try {
            return new ResponseEntity<>(processService.getJefeProcess(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Elimina un proceso", description = "Elimina un proceso por id")
    @Parameter(name = "processId", description = "Id del proceso", required = true)
    @ApiResponse(responseCode = "200", description = "Proceso eliminado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    @ApiResponse(responseCode = "400", description = "Error al eliminar el proceso")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProcess(@RequestParam Integer processId){
        try {
            return new ResponseEntity<>(processService.deleteProcess(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            if (e.getStatusCode() == 404){
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualiza un proceso", description = "Actualiza un proceso asignando un abogado o cambio de estado del proceso")
    @ApiResponse(responseCode = "200", description = "Proceso actualizado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    @ApiResponse(responseCode = "400", description = "Error al actualizar el proceso")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @PutMapping("/update")
    public ResponseEntity<?> updateProcess(@RequestBody ProcessUpdateRequest process){
        try {
            return new ResponseEntity<>(processService.updateProcess(process), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtiene el proceso por id", description = "Obtiene la informacion del proceso por id para el abogado")
    @Parameter(name = "processId", description = "Id del proceso", required = true)
    @ApiResponse(responseCode = "200", description = "Proceso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProcessAbogadoResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener el proceso")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @GetMapping("/get/abogado")
    public ResponseEntity<?> getProcessAbogado(@RequestParam Integer processId){
        try {
            return new ResponseEntity<>(processService.getProcessAbogado(processId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtiene todos los estados de los procesos", description = "Obtiene todos los estados de los procesos")
    @ApiResponse(responseCode = "200", description = "Lista de estado de proceeso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = EstadoProceso.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los estados de los procesos")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'JEFE')")
    @GetMapping("/estadoProceso/get/all")
    public ResponseEntity<?> getAllEstadoProcesos(){
        try {
            return new ResponseEntity<>(processService.getEstadoProcesos(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtiene todos los tipos de procesos", description = "Obtiene todos los tipos de procesos")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de procesos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TipoProceso.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los tipos de procesos")
    @GetMapping("/tipoProceso/get/all")
    public ResponseEntity<?> getAllTipoProcesos(){
        try {
            return new ResponseEntity<>(processService.getTipoProcesos(), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar audiencia", description = "Actualiza el enlace de la audiencia dado el id de la audiencia")
    @Parameter(name = "id", description = "Id de la audiencia", required = true)
    @Parameter(name = "enlace", description = "Enlace de la audiencia", required = true)
    @ApiResponse(responseCode = "200", description = "Audiencia actualizada", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    @ApiResponse(responseCode = "400", description = "Error al actualizar la audiencia")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @PutMapping("/audiencia/update")
    public ResponseEntity<?> updateAudiencia(@RequestParam Integer id, @RequestParam String enlace){
        try {
            return new ResponseEntity<>(processService.updateAudiencia(id, enlace), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            if (e.getStatusCode() == 404){
                return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Agregar audiencia", description = "Agrega una audiencia a un proceso")
    @ApiResponse(responseCode = "200", description = "Audiencia agregada", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    @ApiResponse(responseCode = "400", description = "Error al agregar la audiencia")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'ABOGADO')")
    @PostMapping("/audiencia/add")
    public ResponseEntity<?> addAudiencia(@RequestBody AudienciaRequest audiencia){
        try {
            return new ResponseEntity<>(processService.addAudiencia(audiencia), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

}
