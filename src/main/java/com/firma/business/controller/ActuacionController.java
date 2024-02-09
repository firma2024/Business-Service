package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.ActuacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/business/actuacion")
public class ActuacionController {

    @Autowired
    private ActuacionService actuacionService;
    private Logger loggerActuacion = LoggerFactory.getLogger(ActuacionController.class);

    @Scheduled(cron = "0 0 7 * * ?")
    public void findNewActuaciones() {
        try {
            loggerActuacion.info("Buscando actuaciones nuevas");
            List<ProcesoResponse> process = actuacionService.getProcessService().getProcess();
            List<FindProcess> processFind = new ArrayList<>();

            for (ProcesoResponse procesoResponse : process) {
                FindProcess ac = FindProcess.builder()
                        .number_process(procesoResponse.getNumeroProceso())
                        .file_number(procesoResponse.getNumeroRadicado())
                        .date(procesoResponse.getFechaUltimaActuacion())
                        .build();

                processFind.add(ac);
            }
            List<ActuacionRequest> actuaciones = actuacionService.findNewActuacion(processFind);

            if (actuaciones.isEmpty()){
                loggerActuacion.info("No hay actuaciones nuevas");
                return;
            }

            loggerActuacion.info(actuacionService.saveActuaciones(actuaciones));

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/30 7-9 * * ?") //rango de 7:00 am a 9:00 am cada 30 minutos
    public void sendEmailNewActuacion(){
        try {
            loggerActuacion.info("Enviando correos de actuaciones");
            Set<Actuacion> actuaciones = actuacionService.findActuacionesNotSend();
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
            List<Integer> actSend =  actuacionService.sendEmailActuacion(actuacionesEmail);

            loggerActuacion.info(actuacionService.updateActuacionesSend(actSend));

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getActuacion(@RequestParam Integer id){
        return actuacionService.getActuacion(id);
    }

    @GetMapping("/jefe/get/all/filter")
    public ResponseEntity<?> getActuacionesFilter(@RequestParam Integer procesoId,
                                                  @RequestParam(required = false) String fechaInicioStr,
                                                  @RequestParam(required = false) String fechaFinStr,
                                                  @RequestParam(required = false) String estadoActuacion,
                                                  @RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "5") Integer size){
        return actuacionService.getActuacionesFilter(procesoId, fechaInicioStr, fechaFinStr, estadoActuacion, page, size);
    }

    @GetMapping("/get/all/abogado/filter")
    public ResponseEntity <?> getAllActuacionesByProcesoAbogado(@RequestParam Integer procesoId,
                                                                @RequestParam(required = false) String fechaInicioStr,
                                                                @RequestParam(required = false) String fechaFinStr,
                                                                @RequestParam(required = false) Boolean existeDoc,
                                                                @RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "5") Integer size){
        return actuacionService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size);
    }
}
