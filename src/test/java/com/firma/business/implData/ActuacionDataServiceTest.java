package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.implData.ActuacionDataService;
import com.firma.business.model.Actuacion;
import com.firma.business.model.EstadoActuacion;
import com.firma.business.model.RegistroCorreo;
import com.firma.business.payload.response.PageableActuacionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ActuacionDataServiceTest {

    @InjectMocks
    ActuacionDataService actuacionDataService;
    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateActuacionSuccessfully() throws ErrorDataServiceException {
        Actuacion actuacion = new Actuacion();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        String result = actuacionDataService.updateActuacion(actuacion);

        assertEquals("Success", result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenUpdateActuacionFails() {
        Actuacion actuacion = new Actuacion();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.updateActuacion(actuacion));
    }

    @Test
    void shouldSaveRegistroCorreoSuccessfully() throws ErrorDataServiceException {
        RegistroCorreo registroCorreo = new RegistroCorreo();
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        String result = actuacionDataService.saveRegistroCorreo(registroCorreo);

        assertEquals("Success", result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenSaveRegistroCorreoFails() {
        RegistroCorreo registroCorreo = new RegistroCorreo();
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.saveRegistroCorreo(registroCorreo));
    }

    @Test
    void shouldSaveActuacionesSuccessfully() throws ErrorDataServiceException {
        List<Actuacion> actuaciones = new ArrayList<>();
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        String result = actuacionDataService.saveActuaciones(actuaciones);

        assertEquals("Success", result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenSaveActuacionesFails() {
        List<Actuacion> actuaciones = new ArrayList<>();
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.saveActuaciones(actuaciones));
    }

    @Test
    void shouldFindActuacionesNotSendSuccessfully() throws ErrorDataServiceException {
        Actuacion[] actuaciones = new Actuacion[0];
        when(restTemplate.getForEntity(anyString(), eq(Actuacion[].class)))
                .thenReturn(new ResponseEntity<>(actuaciones, HttpStatus.OK));

        Set<Actuacion> result = actuacionDataService.findActuacionesNotSend();

        assertEquals(Set.of(actuaciones), result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenFindActuacionesNotSendFails() {
        when(restTemplate.getForEntity(anyString(), eq(Actuacion[].class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.findActuacionesNotSend());
    }

    @Test
    void shouldUpdateActuacionesSendSuccessfully() throws ErrorDataServiceException {
        List<Integer> actuaciones = Arrays.asList(1, 2, 3);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        String result = actuacionDataService.updateActuacionesSend(actuaciones);

        assertEquals("Success", result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenUpdateActuacionesSendFails() {
        List<Integer> actuaciones = Arrays.asList(1, 2, 3);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.updateActuacionesSend(actuaciones));
    }

    @Test
    void shouldGetActuacionSuccessfully() throws ErrorDataServiceException {
        Actuacion actuacion = new Actuacion();
        when(restTemplate.getForEntity(anyString(), eq(Actuacion.class)))
                .thenReturn(new ResponseEntity<>(actuacion, HttpStatus.OK));

        Actuacion result = actuacionDataService.getActuacion(1);

        assertEquals(actuacion, result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenGetActuacionFails() {
        when(restTemplate.getForEntity(anyString(), eq(Actuacion.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.getActuacion(1));
    }

    @Test
    void shouldGetActuacionesFilterSuccessfully() throws ErrorDataServiceException {
        PageableActuacionResponse pageableActuacionResponse = new PageableActuacionResponse();
        when(restTemplate.getForEntity(anyString(), eq(PageableActuacionResponse.class)))
                .thenReturn(new ResponseEntity<>(pageableActuacionResponse, HttpStatus.OK));

        PageableActuacionResponse result = actuacionDataService.getActuacionesFilter(1, "2022-01-01", "2022-12-31", "estado", 1, 10);

        assertEquals(pageableActuacionResponse, result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenGetActuacionesFilterFails() {
        when(restTemplate.getForEntity(anyString(), eq(PageableActuacionResponse.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.getActuacionesFilter(1, "2022-01-01", "2022-12-31", "estado", 1, 10));
    }

    @Test
    void shouldGetActuacionesByProcesoAbogadoSuccessfully() throws ErrorDataServiceException {
        Integer procesoId = 1;
        String fechaInicioStr = "2022-01-01";
        String fechaFinStr = "2022-12-31";
        Boolean existeDoc = true;
        Integer page = 1;
        Integer size = 10;
        PageableActuacionResponse pageableActuacionResponse = new PageableActuacionResponse();
        when(restTemplate.getForEntity(anyString(), eq(PageableActuacionResponse.class)))
                .thenReturn(new ResponseEntity<>(pageableActuacionResponse, HttpStatus.OK));

        PageableActuacionResponse result = actuacionDataService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size);

        assertEquals(pageableActuacionResponse, result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenGetActuacionesByProcesoAbogadoFails() {
        Integer procesoId = 1;
        String fechaInicioStr = "2022-01-01";
        String fechaFinStr = "2022-12-31";
        Boolean existeDoc = true;
        Integer page = 1;
        Integer size = 10;
        when(restTemplate.getForEntity(anyString(), eq(PageableActuacionResponse.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size));
    }

    @Test
    void shouldFindEstadoActuacionByNameSuccessfully() throws ErrorDataServiceException {
        String state = "state";
        EstadoActuacion estadoActuacion = new EstadoActuacion();
        when(restTemplate.getForEntity(anyString(), eq(EstadoActuacion.class)))
                .thenReturn(new ResponseEntity<>(estadoActuacion, HttpStatus.OK));

        EstadoActuacion result = actuacionDataService.findEstadoActuacionByName(state);

        assertEquals(estadoActuacion, result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenFindEstadoActuacionByNameFails() {
        String state = "state";
        when(restTemplate.getForEntity(anyString(), eq(EstadoActuacion.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.findEstadoActuacionByName(state));
    }

    @Test
    void shouldFindLastActuacionSuccessfully() throws ErrorDataServiceException {
        Integer processid = 1;
        Actuacion actuacion = new Actuacion();
        when(restTemplate.getForEntity(anyString(), eq(Actuacion.class)))
                .thenReturn(new ResponseEntity<>(actuacion, HttpStatus.OK));

        Actuacion result = actuacionDataService.findLastActuacion(processid);

        assertEquals(actuacion, result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenFindLastActuacionFails() {
        Integer processid = 1;
        when(restTemplate.getForEntity(anyString(), eq(Actuacion.class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.findLastActuacion(processid));
    }

    @Test
    void shouldFindByNoVistoSuccessfully() throws ErrorDataServiceException {
        Integer firmaId = 1;
        Actuacion[] actuaciones = new Actuacion[0];
        when(restTemplate.getForEntity(anyString(), eq(Actuacion[].class)))
                .thenReturn(new ResponseEntity<>(actuaciones, HttpStatus.OK));

        List<Actuacion> result = actuacionDataService.findByNoVisto(firmaId);

        assertEquals(List.of(actuaciones), result);
    }

    @Test
    void shouldThrowErrorDataServiceExceptionWhenFindByNoVistoFails() {
        Integer firmaId = 1;
        when(restTemplate.getForEntity(anyString(), eq(Actuacion[].class)))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(ErrorDataServiceException.class, () -> actuacionDataService.findByNoVisto(firmaId));
    }

}