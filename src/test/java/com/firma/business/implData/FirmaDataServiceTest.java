package com.firma.business.implData;



import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.implData.FirmaDataService;
import com.firma.business.model.Empleado;
import com.firma.business.model.Firma;
import com.firma.business.payload.request.FirmaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class FirmaDataServiceTest {

    @InjectMocks
    FirmaDataService firmaDataService;
    @Mock
    RestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFirmaByUserReturnsFirma() throws ErrorDataServiceException {
        Firma expectedFirma = new Firma();
        when(restTemplate.getForEntity(any(String.class), eq(Firma.class))).thenReturn(ResponseEntity.ok(expectedFirma));

        Firma actualFirma = firmaDataService.getFirmaByUser("username");

        assertEquals(expectedFirma, actualFirma);
    }

    @Test
    void getFirmaByUserThrowsErrorDataServiceException() {
        when(restTemplate.getForEntity(any(String.class), eq(Firma.class))).thenThrow(RuntimeException.class);

        assertThrows(ErrorDataServiceException.class, () -> firmaDataService.getFirmaByUser("username"));
    }

    @Test
    void saveFirmaReturnsString() throws ErrorDataServiceException {
        FirmaRequest request = new FirmaRequest();
        when(restTemplate.exchange(any(String.class), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("Success"));

        String response = firmaDataService.saveFirma(request);

        assertEquals("Success", response);
    }

    @Test
    void saveFirmaThrowsErrorDataServiceException() {
        FirmaRequest request = new FirmaRequest();
        when(restTemplate.exchange(any(String.class), any(), any(), eq(String.class))).thenThrow(RuntimeException.class);

        assertThrows(ErrorDataServiceException.class, () -> firmaDataService.saveFirma(request));
    }

    @Test
    void findFirmaByIdReturnsFirma() throws ErrorDataServiceException {
        Firma expectedFirma = new Firma();
        when(restTemplate.getForEntity(any(String.class), eq(Firma.class))).thenReturn(ResponseEntity.ok(expectedFirma));

        Firma actualFirma = firmaDataService.findFirmaById(1);

        assertEquals(expectedFirma, actualFirma);
    }

    @Test
    void findFirmaByIdThrowsErrorDataServiceException() {
        when(restTemplate.getForEntity(any(String.class), eq(Firma.class))).thenThrow(RuntimeException.class);

        assertThrows(ErrorDataServiceException.class, () -> firmaDataService.findFirmaById(1));
    }

    @Test
    void findEmpleadoByUsuarioReturnsEmpleado() throws ErrorDataServiceException {
        Empleado expectedEmpleado = new Empleado();
        when(restTemplate.getForEntity(any(String.class), eq(Empleado.class))).thenReturn(ResponseEntity.ok(expectedEmpleado));

        Empleado actualEmpleado = firmaDataService.findEmpleadoByUsuario(1);

        assertEquals(expectedEmpleado, actualEmpleado);
    }

    @Test
    void findEmpleadoByUsuarioThrowsErrorDataServiceException() {
        when(restTemplate.getForEntity(any(String.class), eq(Empleado.class))).thenThrow(RuntimeException.class);

        assertThrows(ErrorDataServiceException.class, () -> firmaDataService.findEmpleadoByUsuario(1));
    }


}