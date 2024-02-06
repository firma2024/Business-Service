package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.data.DataService;
import com.firma.business.service.integration.IntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/actuacion")
public class ActuacionController {

    @Autowired
    private DataService dataService;
    @Autowired
    private IntegrationService integrationService;
    private Logger loggerActuacion = LoggerFactory.getLogger(ActuacionController.class);

    //@Scheduled(fixedRate = 50000)
    @Scheduled(cron = "0 0 7 * * ?")
    public void findNewActuaciones() {
        try {
            loggerActuacion.info("Buscando actuaciones nuevas");
            List <ProcesoResponse> process = dataService.getProcess();
            List<FindProcess> processFind = new ArrayList<>();

            for (ProcesoResponse procesoResponse : process) {
                FindProcess ac = FindProcess.builder()
                        .number_process(procesoResponse.getNumeroProceso())
                        .file_number(procesoResponse.getNumeroRadicado())
                        .date(procesoResponse.getFechaUltimaActuacion())
                        .build();

                processFind.add(ac);
            }
            List<ActuacionRequest> actuaciones = integrationService.findNewActuacion(processFind);

            if (actuaciones.isEmpty()){
                loggerActuacion.info("No hay actuaciones nuevas");
                return;
            }

            String response = dataService.saveActuaciones(actuaciones);
            loggerActuacion.info(response);

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    //@Scheduled(fixedRate = 50000)
    @Scheduled(cron = "0 0/30 7-9 * * ?") //rango de 7:00 am a 9:00 am cada 30 minutos
    public void sendEmailNewActuacion(){
        try {
            loggerActuacion.info("Enviando correos de actuaciones");
            Set<Actuacion> actuaciones = dataService.findActuacionesNotSend();
            if (actuaciones.isEmpty()){
                loggerActuacion.info("No hay actuaciones para enviar");
                return;
            }

            List <ActuacionEmail> actuacionesEmail = new ArrayList<>();
            for (Actuacion actuacion : actuaciones) {
                ActuacionEmail actuacionEmail = ActuacionEmail.builder()
                        .id(actuacion.getId())
                        .demandante(actuacion.getDemandante())
                        .demandado(actuacion.getDemandado())
                        .actuacion(actuacion.getActuacion())
                        .radicado(actuacion.getRadicado())
                        .anotacion(actuacion.getAnotacion())
                        .fechaActuacion(actuacion.getFechaActuacion())
                        .emailAbogado(actuacion.getEmailAbogado())
                        .nameAbogado(actuacion.getNameAbogado())
                        .link("http://localhost:3000/actuacion/" + actuacion.getId())
                        .build();

                actuacionesEmail.add(actuacionEmail);
            }
            List<Integer> actSend =  integrationService.sendEmailActuacion(actuacionesEmail);

            String response = dataService.updateActuacionesSend(actSend);
            loggerActuacion.info(response);

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity <?> getActuacion(@RequestParam Integer id){
        try {
            Actuacion ac = dataService.getActuacionById(id);
            return new ResponseEntity<>(ac, HttpStatus.OK);

        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/jefe/get/all/filter")
    public ResponseEntity<?> getActuacionesFilter(@RequestParam Integer procesoId,
                                                  @RequestParam(required = false) String fechaInicioStr,
                                                  @RequestParam(required = false) String fechaFinStr,
                                                  @RequestParam(required = false) String estadoActuacion,
                                                  @RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "2") Integer size){
        try {
            PageableResponse<Actuacion> response = dataService.getActuacionesFilter(procesoId, fechaInicioStr, fechaFinStr, estadoActuacion, page, size);
            return ResponseEntity.ok(response);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/all/abogado/filter")
    public ResponseEntity <?> getAllActuacionesByProcesoAbogado(@RequestParam Integer procesoId,
                                                                @RequestParam(required = false) String fechaInicioStr,
                                                                @RequestParam(required = false) String fechaFinStr,
                                                                @RequestParam(required = false) Boolean existeDoc,
                                                                @RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "2") Integer size){

        try {
            PageableResponse<Actuacion> response = dataService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size);
            return ResponseEntity.ok(response);
        } catch (ErrorDataServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
