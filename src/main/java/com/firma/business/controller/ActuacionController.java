package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.data.DataService;
import com.firma.business.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    //@Scheduled(fixedRate = 50000)
    @Scheduled(cron = "0 0 7 * * ?")
    public void findNewActuaciones() {
        try {
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

            if (actuaciones.size() == 0){
                System.out.println("No hay actuaciones nuevas");
                return;
            }

            String response = dataService.saveActuaciones(actuaciones);
            System.out.println(response);

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    //@Scheduled(fixedRate = 50000)
    @Scheduled(cron = "0 0/30 7-9 * * ?") //rango de 7:00 am a 9:00 am cada 30 minutos
    public void sendEmailNewActuacion(){
        try {
            Set<Actuacion> actuaciones = dataService.findActuacionesNotSend();
            if (actuaciones.size() == 0){
                System.out.println("No hay actuaciones que enviar");
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

            System.out.println(response);

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

}
