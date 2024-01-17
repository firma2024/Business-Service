package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Despacho;
import com.firma.business.payload.DespachoResponse;
import com.firma.business.payload.EnlaceRequest;
import com.firma.business.service.data.DataService;
import com.firma.business.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Set;

@Controller
public class DespachoController {

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private DataService dataService;

    @Scheduled(fixedRate = 600000)
    public void updateDespacho(){
        try {
            Integer year = LocalDate.now().getYear();
            Set<Despacho> despachos = dataService.findAllDespachosWithOutLink(year);

            for (Despacho despacho : despachos) {
                System.out.println(despacho.getNombre());
                DespachoResponse url = integrationService.findUrlDespacho(despacho.getNombre());
                EnlaceRequest en = EnlaceRequest.builder()
                        .url(url.getUrl_despacho())
                        .despachoid(despacho.getId())
                        .fechaconsulta(LocalDate.now())
                        .build();
                String response = dataService.saveEnlace(en);
                System.out.println(response);
            }

        } catch (ErrorIntegrationServiceException | ErrorDataServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}
