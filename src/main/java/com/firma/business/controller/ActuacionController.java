package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.Actuacion;
import com.firma.business.payload.request.ActuacionEmailRequest;
import com.firma.business.payload.request.ActuacionRequest;
import com.firma.business.payload.request.FindProcessRequest;
import com.firma.business.payload.response.ActuacionResponse;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.response.ProcesoResponse;
import com.firma.business.service.ActuacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/business/actuacion")
public class ActuacionController {

    @Autowired
    private ActuacionService actuacionService;
    private Logger loggerActuacion = LoggerFactory.getLogger(ActuacionController.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Value("${api.presentation.url}")
    private String apiPresentationUrl;

    @Scheduled(cron = "0 0 7 * * ?")
    public void findNewActuaciones() {
        try {
            loggerActuacion.info("Buscando actuaciones nuevas");
            List<ProcesoResponse> process = actuacionService.getAllProcess();
            List<FindProcessRequest> processFind = new ArrayList<>();

            for (ProcesoResponse procesoResponse : process) {
                FindProcessRequest ac = FindProcessRequest.builder()
                        .number_process(procesoResponse.getNumeroProceso())
                        .file_number(procesoResponse.getNumeroRadicado())
                        .date(procesoResponse.getFechaUltimaActuacion())
                        .build();

                processFind.add(ac);
            }
            List<ActuacionRequest> actuaciones = actuacionService.findNewActuacion(processFind);

            if (actuaciones.isEmpty()) {
                loggerActuacion.info("No hay actuaciones nuevas");
                return;
            }

            loggerActuacion.info(actuacionService.saveActuaciones(actuaciones));

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            loggerActuacion.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/30 7-9 * * ?") //rango de 7:00 am a 9:00 am cada 30 minutos
    public void sendEmailNewActuacion() {
        try {
            loggerActuacion.info("Enviando correos de actuaciones");
            Set<Actuacion> actuaciones = actuacionService.findActuacionesNotSend();
            if (actuaciones.isEmpty()) {
                loggerActuacion.info("No hay actuaciones para enviar");
                return;
            }

            List<ActuacionEmailRequest> actuacionesEmail = new ArrayList<>();
            for (Actuacion actuacion : actuaciones) {
                ActuacionEmailRequest actuacionEmail = ActuacionEmailRequest.builder()
                        .id(actuacion.getId())
                        .actuacion(actuacion.getActuacion())
                        .radicado(actuacion.getProceso().getRadicado())
                        .anotacion(actuacion.getAnotacion())
                        .fechaActuacion(actuacion.getFechaactuacion().format(formatter))
                        .emailAbogado(actuacion.getProceso().getEmpleado().getUsuario().getCorreo())
                        .nameAbogado(actuacion.getProceso().getEmpleado().getUsuario().getNombres())
                        .link(String.format("%s/actuacion/?id=%d", apiPresentationUrl, actuacion.getId()))
                        .build();

                actuacionesEmail.add(actuacionEmail);
            }
            List<Integer> actSend = actuacionService.sendEmailActuacion(actuacionesEmail);

            actuacionService.updateActuacionesSend(actSend);

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            loggerActuacion.error(e.getMessage());
        }
    }

    @Operation(summary = "Obtener actuacion por id", description = "Obtiene informacion de una actuacion por id.")
    @Parameter(name = "id", description = "Id de la actuacion", required = true)
    @ApiResponse(responseCode = "200", description = "Se retorna la entidad de la actuacion", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ActuacionResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener la actuacion")
    @GetMapping("/get")
    public ResponseEntity<?> getActuacion(@RequestParam Integer id) {
        try {
            return new ResponseEntity<>(actuacionService.getActuacion(id), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Filtro de actuaciones proceso jefe", description = "Obtiene las actuaciones de un proceso dado un filtro en caso de enviar ningun filtro se consultan todas las actuaciones.")
    @Parameter(name = "procesoId", description = "Id del proceso", required = true)
    @Parameter(name = "fechaInicioStr", description = "Fecha de inicio", required = false)
    @Parameter(name = "fechaFinStr", description = "Fecha de fin", required = false)
    @Parameter(name = "estadoActuacion", description = "Estado de la actuacion (Vista, No vista)", required = false)
    @Parameter(name = "page", description = "Pagina", required = false)
    @Parameter(name = "size", description = "Tamaño de la pagina", required = false)
    @ApiResponse(responseCode = "200", description = "Se retorna la lista de actuaciones", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageableResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener las actuaciones")
    @GetMapping("/jefe/get/all/filter")
    public ResponseEntity<?> getActuacionesFilter(@RequestParam Integer procesoId,
                                                  @RequestParam(required = false) String fechaInicioStr,
                                                  @RequestParam(required = false) String fechaFinStr,
                                                  @RequestParam(required = false) String estadoActuacion,
                                                  @RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "5") Integer size) {
        try {
            return new ResponseEntity<>(actuacionService.getActuacionesFilter(procesoId, fechaInicioStr, fechaFinStr, estadoActuacion, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Filtro de actuaciones proceso abogado", description = "Obtiene las actuaciones de un proceso dado un filtro en caso de enviar ningun filtro se consultan todas las actuaciones.")
    @Parameter(name = "procesoId", description = "Id del proceso", required = true)
    @Parameter(name = "fechaInicioStr", description = "Fecha de inicio", required = false)
    @Parameter(name = "fechaFinStr", description = "Fecha de fin", required = false)
    @Parameter(name = "existeDoc", description = "Existe documento", required = false)
    @Parameter(name = "page", description = "Pagina", required = false)
    @Parameter(name = "size", description = "Tamaño de la pagina", required = false)
    @ApiResponse(responseCode = "200", description = "Se retorna la lista de actuaciones", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageableResponse.class))})
    @GetMapping("/get/all/abogado/filter")
    public ResponseEntity<?> getAllActuacionesByProcesoAbogado(@RequestParam Integer procesoId,
                                                               @RequestParam(required = false) String fechaInicioStr,
                                                               @RequestParam(required = false) String fechaFinStr,
                                                               @RequestParam(required = false) Boolean existeDoc,
                                                               @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size) {
        try {
            return new ResponseEntity<>(actuacionService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar estado de visualizacion actuacion", description = "Actualiza el estado de visualizacion de la actuacion dado el id de la actuacion, cuando el abogado ve la actuacion")
    @Parameter(name = "actionId", description = "Id de la actuacion", required = true)
    @ApiResponse(responseCode = "200", description = "Estado de visualizacion actualizado")
    @ApiResponse(responseCode = "400", description = "Error al actualizar el estado de visualizacion")
    @PutMapping("/update/state")
    public ResponseEntity<?> updateStateActuacion(@RequestParam Integer actionId){
        try {
            return new ResponseEntity<>(actuacionService.updateActuacion(actionId), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
