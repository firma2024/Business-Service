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
    void saveProcessReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Process saved successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.saveProcess(new ProcessDataRequest());

        assertEquals(expectedResponse, actualResponse);
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
    void updateProcessReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Process updated successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.updateProcess(new Proceso());

        assertEquals(expectedResponse, actualResponse);
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
    void getStateProcessesJefeReturnsExpectedResult() throws ErrorDataServiceException {
        Proceso[] expectedProcesses = new Proceso[]{new Proceso(), new Proceso()};
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        List<Proceso> actualProcesses = processDataService.getStateProcessesJefe("OPEN", 1);

        assertEquals(List.of(expectedProcesses), actualProcesses);
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
    void getStateProcessesAbogadoReturnsExpectedResult() throws ErrorDataServiceException {
        Proceso[] expectedProcesses = new Proceso[]{new Proceso(), new Proceso()};
        when(restTemplate.getForEntity(any(String.class), eq(Proceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        List<Proceso> actualProcesses = processDataService.getStateProcessesAbogado("name", "userName");

        assertEquals(List.of(expectedProcesses), actualProcesses);
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
    void getEstadoProcesosReturnsExpectedResult() throws ErrorDataServiceException {
        EstadoProceso[] expectedProcesses = new EstadoProceso[]{new EstadoProceso(), new EstadoProceso()};
        when(restTemplate.getForEntity(any(String.class), eq(EstadoProceso[].class)))
                .thenReturn(ResponseEntity.ok(expectedProcesses));

        List<EstadoProceso> actualProcesses = processDataService.getEstadoProcesos();

        assertEquals(List.of(expectedProcesses), actualProcesses);
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
    void findAllDespachosWithOutLinkReturnsExpectedResult() throws ErrorDataServiceException {
        Despacho[] expectedDespachos = new Despacho[]{new Despacho(), new Despacho()};
        when(restTemplate.getForEntity(any(String.class), eq(Despacho[].class)))
                .thenReturn(ResponseEntity.ok(expectedDespachos));

        Set<Despacho> actualDespachos = processDataService.findAllDespachosWithOutLink(2022);

        assertEquals(Set.of(expectedDespachos), actualDespachos);
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
    void saveEnlaceReturnsExpectedResult() throws ErrorDataServiceException {
        String expectedResponse = "Enlace saved successfully";
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        String actualResponse = processDataService.saveEnlace(new Enlace());

        assertEquals(expectedResponse, actualResponse);
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
    void findDespachoByNombreSuccesfully() throws ErrorDataServiceException {
        Despacho expected = new Despacho(1, "test");
        when(restTemplate.getForEntity(any(String.class), eq(Despacho.class)))
                .thenReturn(ResponseEntity.ok(expected));

        Despacho actual = processDataService.findDespachoByNombre("test");

        assertEquals(expected, actual);

    }

    @Test
    void findEstadoProcesoByNombreSuccesfully() throws ErrorDataServiceException {
        EstadoProceso expected = new EstadoProceso(1, "test");
        when(restTemplate.getForEntity(any(String.class), eq(EstadoProceso.class)))
                .thenReturn(ResponseEntity.ok(expected));

        EstadoProceso actual = processDataService.findEstadoProcesoByNombre("test");

        assertEquals(expected, actual);

    }

    @Test
    void findAllAudienciasByProcesoSuccessfully() throws ErrorDataServiceException {
        Audiencia[] expected = new Audiencia[]{new Audiencia(), new Audiencia()};
        when(restTemplate.getForEntity(any(String.class), eq(Audiencia[].class)))
                .thenReturn(ResponseEntity.ok(expected));

        Set<Audiencia> actual = processDataService.findAllAudienciasByProceso(1);

        assertEquals(Set.of(expected), actual);
    }

    @Test
    void findDespachoByIdSuccesfully() throws ErrorDataServiceException {
        Despacho expected = new Despacho(1, "test");
        when(restTemplate.getForEntity(any(String.class), eq(Despacho.class)))
                .thenReturn(ResponseEntity.ok(expected));

        Despacho actual = processDataService.findDespachoById(1);

        assertEquals(expected, actual);

    }

    @Test
    void findByRadicadoSuccesfully() throws ErrorDataServiceException {
        Proceso expected = new Proceso();
        when(restTemplate.getForEntity(any(String.class), eq(Proceso.class)))
                .thenReturn(ResponseEntity.ok(expected));

        Proceso actual = processDataService.findByRadicado("test");

        assertEquals(expected, actual);

    }

    @Test
    void findByDespachoAndYearSuccessfully() throws ErrorDataServiceException {
        Enlace expected = new Enlace();
        when(restTemplate.getForEntity(any(String.class), eq(Enlace.class)))
                .thenReturn(ResponseEntity.ok(expected));

        Enlace actual = processDataService.findByDespachoAndYear(1, "2022");

        assertEquals(expected, actual);

    }

}