package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.Empleado;
import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.service.data.intf.IFirmaDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FirmaServiceTest {

    @InjectMocks
    FirmaService firmaService;
    @Mock
    IFirmaDataService firmaDataService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getFirmaByUser() throws ErrorDataServiceException {
        String username = "test";
        Firma fm = new Firma(1, "test firma", "test address");
        when(firmaDataService.getFirmaByUser(username)).thenReturn(fm);

        Firma response = firmaService.getFirmaByUser(username);
        assertEquals (fm, response);
    }

    @Test
    void saveFirma() throws ErrorDataServiceException {
        FirmaRequest firmaRequest = FirmaRequest.builder()
                .nombre("test")
                .direccion("test")
                .build();

        when(firmaDataService.saveFirma(firmaRequest)).thenReturn("Firma guardada");
        MessageResponse response = firmaService.saveFirma(firmaRequest);
        assertEquals ("Firma guardada", response.getMessage());
    }

    @Test
    void findFirmaById() throws ErrorDataServiceException {
        Integer id = 1;
        Firma fm = new Firma(1, "test firma", "test address");
        when(firmaDataService.findFirmaById(id)).thenReturn(fm);

        Firma response = firmaService.findFirmaById(id);
        assertEquals (fm, response);
    }

    @Test
    void findEmpleadoByUsuario() throws ErrorDataServiceException {
        Empleado emp = new Empleado();
        Integer id = 1;
        when(firmaDataService.findEmpleadoByUsuario(id)).thenReturn(emp);

        Empleado response = firmaService.findEmpleadoByUsuario(id);
        assertEquals (emp, response);
    }
}