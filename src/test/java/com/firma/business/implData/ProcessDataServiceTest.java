package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.implData.ProcessDataService;
import com.firma.business.model.*;
import com.firma.business.payload.request.ProcessDataRequest;
import com.firma.business.payload.response.PageableProcessResponse;
import com.firma.business.payload.response.ProcessAbogadoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProcessDataServiceTest {

    @InjectMocks
    ProcessDataService processDataService;
    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProcessReturnsExpectedResult() throws ErrorDataServiceException {
        Proceso[] expectedProcesses = new Proceso[]{new Proceso(), new Proceso()};
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        Set<Proceso> actualProcesses = processDataService.getProcess();

        assertEquals(Set.of(expectedProcesses), actualProcesses);
    }

    @Test
    void getProcessThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getProcess());
    }

    @Test
    void saveProcessReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Process saved successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.saveProcess(new ProcessDataRequest());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void saveProcessThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.saveProcess(new ProcessDataRequest()));
    }

    @Test
    void getProcessByFilterReturnsExpectedResult() throws ErrorDataServiceException {
        PageableProcessResponse expectedResponse = new PageableProcessResponse();
        when(restTemplate.getForEntity(any(String.class), eq(PageableProcessResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        PageableProcessResponse actualResponse = processDataService.getProcessByFilter("2022-01-01", 1, "2022-12-31", List.of("OPEN"), "TYPE1", 0, 10);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getProcessByFilterThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(PageableProcessResponse.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getProcessByFilter("2022-01-01", 1, "2022-12-31", List.of("OPEN"), "TYPE1", 0, 10));
    }

    @Test
    void deleteProcessThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.deleteProcess(1));
    }

    @Test
    void updateProcessReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Process updated successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.updateProcess(new Proceso());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void updateProcessThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.updateProcess(new Proceso()));
    }

    @Test
    void getProcessByAbogadoReturnsExpectedResult() throws ErrorDataServiceException {
        PageableProcessResponse expectedResponse = new PageableProcessResponse();
        when(restTemplate.getForEntity(any(String.class), eq(PageableProcessResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        PageableProcessResponse actualResponse = processDataService.getProcessByAbogado(1, "2022-01-01", "2022-12-31", List.of("OPEN"), "TYPE1", 0, 10);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getProcessByAbogadoThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(PageableProcessResponse.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getProcessByAbogado(1, "2022-01-01", "2022-12-31", List.of("OPEN"), "TYPE1", 0, 10));
    }

    @Test
    void getStateProcessesJefeReturnsExpectedResult() throws ErrorDataServiceException {
        Proceso[] expectedProcesses = new Proceso[]{new Proceso(), new Proceso()};
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        List<Proceso> actualProcesses = processDataService.getStateProcessesJefe("OPEN", 1);

        assertEquals(List.of(expectedProcesses), actualProcesses);
    }

    @Test
    void getStateProcessesJefeThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getStateProcessesJefe("OPEN", 1));
    }

    @Test
    void getProcessByIdReturnsExpectedResult() throws ErrorDataServiceException {
        Proceso expectedProcess = new Proceso();
        when(restTemplate.getForEntity(any(String.class), eq(Proceso.class)))
                .thenReturn(ResponseEntity.ok(expectedProcess));

        Proceso actualProcess = processDataService.getProcessById(1);

        assertEquals(expectedProcess, actualProcess);
    }

    @Test
    void getProcessByIdThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(Proceso.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getProcessById(1));
    }

    @Test
    void getStateProcessesAbogadoReturnsExpectedResult() throws ErrorDataServiceException {
        Proceso[] expectedProcesses = new Proceso[]{new Proceso(), new Proceso()};
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        List<Proceso> actualProcesses = processDataService.getStateProcessesAbogado("name", "userName");

        assertEquals(List.of(expectedProcesses), actualProcesses);
    }

    @Test
    void getStateProcessesAbogadoThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getStateProcessesAbogado("name", "userName"));
    }

    @Test
    void getProcessAbogadoReturnsExpectedResult() throws ErrorDataServiceException {
        ProcessAbogadoResponse expectedResponse = new ProcessAbogadoResponse();
        when(restTemplate.getForEntity(any(String.class), eq(ProcessAbogadoResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        ProcessAbogadoResponse actualResponse = processDataService.getProcessAbogado(1);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getProcessAbogadoThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(ProcessAbogadoResponse.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getProcessAbogado(1));
    }

    @Test
    void getEstadoProcesosReturnsExpectedResult() throws ErrorDataServiceException {
        EstadoProceso[] expectedProcesses = new EstadoProceso[]{new EstadoProceso(), new EstadoProceso()};
        when(restTemplate.getForEntity(any(String.class), eq(EstadoProceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        List<EstadoProceso> actualProcesses = processDataService.getEstadoProcesos();

        assertEquals(List.of(expectedProcesses), actualProcesses);
    }

    @Test
    void getEstadoProcesosThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(EstadoProceso[].class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getEstadoProcesos());
    }

    @Test
    void updateAudienciaThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.updateAudiencia(1, "newLink"));
    }

    @Test
    void addAudienciaReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Audiencia added successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.addAudiencia(new Audiencia());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void addAudienciaThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.addAudiencia(new Audiencia()));
    }

    @Test
    void findAllDespachosWithOutLinkReturnsExpectedResult() throws ErrorDataServiceException {
        Despacho[] expectedDespachos = new Despacho[]{new Despacho(), new Despacho()};
        when(restTemplate.getForEntity(any(String.class), eq(Despacho[].class)))
                .thenReturn(ResponseEntity.ok(expectedDespachos));

        Set<Despacho> actualDespachos = processDataService.findAllDespachosWithOutLink(2022);

        assertEquals(Set.of(expectedDespachos), actualDespachos);
    }

    @Test
    void findAllDespachosWithOutLinkThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(Despacho[].class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.findAllDespachosWithOutLink(2022));
    }

    @Test
    void getTipoProcesosReturnsExpectedResult() throws ErrorDataServiceException {
        TipoProceso[] expectedTipoProcesos = new TipoProceso[]{new TipoProceso(), new TipoProceso()};
        when(restTemplate.getForEntity(any(String.class), eq(TipoProceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedTipoProcesos));

        List<TipoProceso> actualTipoProcesos = processDataService.getTipoProcesos();

        assertEquals(List.of(expectedTipoProcesos), actualTipoProcesos);
    }

    @Test
    void getTipoProcesosThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(TipoProceso[].class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.getTipoProcesos());
    }

    @Test
    void saveEnlaceReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Enlace saved successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.saveEnlace(new Enlace());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void saveEnlaceThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.saveEnlace(new Enlace()));
    }

    @Test
    void findTipoProcesoByNombreReturnsExpectedResult() throws ErrorDataServiceException {
        TipoProceso expectedTipoProceso = new TipoProceso();
        when(restTemplate.getForEntity(any(String.class), eq(TipoProceso.class)))
                .thenReturn(ResponseEntity.ok(expectedTipoProceso));

        TipoProceso actualTipoProceso = processDataService.findTipoProcesoByNombre("tipoProceso");

        assertEquals(expectedTipoProceso, actualTipoProceso);
    }

    @Test
    void findTipoProcesoByNombreThrowsErrorDataServiceExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(any(String.class), eq(TipoProceso.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(ErrorDataServiceException.class, () -> processDataService.findTipoProcesoByNombre("tipoProceso"));
    }

}