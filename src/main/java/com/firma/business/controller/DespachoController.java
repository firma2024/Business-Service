package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.Despacho;
import com.firma.business.payload.DespachoResponse;
import com.firma.business.service.data.DataService;
import com.firma.business.service.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //@Scheduled(fixedRate = 80000)
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDespacho(){
        try {
            Set<Despacho> despachos = dataService.findAllDespachos();

            for (Despacho despacho : despachos) {
                System.out.println(despacho.getNombre());
                DespachoResponse url = integrationService.findUrlDespacho(despacho.getNombre());
                despacho.setFechaconsulta(LocalDate.now());
                despacho.setUrl(url.getUrl_despacho());
                String response = dataService.updateDespacho(despacho);
                System.out.println(response);
            }

        } catch (ErrorIntegrationServiceException | ErrorDataServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}
