package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.*;
import com.firma.business.payload.request.*;
import com.firma.business.payload.response.*;
import com.firma.business.intfData.IProcessDataService;
import com.firma.business.intfIntegration.IProcessIntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessServiceTest {

    @InjectMocks
    ProcessService processService;
    @Mock
    IProcessDataService processDataService;
    @Mock
    IProcessIntegrationService processIntegrationService;
    @Mock
    FirmaService firmaService;
    @Mock
    ActuacionService actuacionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProcess() throws ErrorDataServiceException {
        Empleado empleado = Empleado.builder()
                .firma(new Firma(1, "test firma", "test address"))
                .build();

        ActuacionRequest actuacionRequest = ActuacionRequest.builder()
                .anotacion("test")
                .nombreActuacion("test actuacion")
                .fechaActuacion("2021-10-10T00:00:00")
                .fechaRegistro("2021-10-10T00:00:00")
                .fechaInicia("2021-10-10T00:00:00")
                .fechaFinaliza("2021-10-10T00:00:00")
                .existDocument(false)
                .build();
        List<ActuacionRequest> actuaciones = List.of(actuacionRequest);

        ProcessRequest processRequest = ProcessRequest.builder()
                .numeroRadicado("1312312312312")
                .idProceso(new BigInteger("32323"))
                .sujetos("test")
                .tipoProceso("test")
                .despacho("test")
                .fechaRadicacion("2021-10-10T00:00:00")
                .ubicacionExpediente("test")
                .actuaciones(actuaciones)
                .idAbogado(1)
                .build();
        EstadoProceso estadoProceso = new EstadoProceso(1, "estado");
        EstadoActuacion estadoActuacion = new EstadoActuacion(1, "Visto");

        when(firmaService.findEmpleadoByUsuario(processRequest.getIdAbogado())).thenReturn(empleado);
        when(processDataService.findTipoProcesoByNombre(processRequest.getTipoProceso())).thenReturn(null);
        when(processDataService.findDespachoByNombre(processRequest.getDespacho())).thenReturn(null);
        when(processDataService.findEstadoProcesoByNombre("Activo")).thenReturn(estadoProceso);
        when(actuacionService.findEstadoActuacionByName("Visto")).thenReturn(estadoActuacion);
        when(processDataService.saveProcess(any())).thenReturn("Proceso guardado");

        MessageResponse response = processService.saveProcess(processRequest);
        assertEquals("Proceso guardado", response.getMessage());
        assertDoesNotThrow(() -> processService.saveProcess(processRequest));

    }

    @Test
    void getJefeProcess() throws ErrorDataServiceException {
        Integer processid = 1;
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombres("test name")
                .build();
        Empleado empleado = Empleado.builder()
                .usuario(usuario)
                .build();
        EstadoProceso estadoProceso = new EstadoProceso(1, "Activo");
        TipoProceso tipoProceso = TipoProceso.builder()
                .id(1)
                .nombre("test")
                .build();
        Proceso proceso = Proceso.builder()
                .id(1)
                .radicado("1231233345534")
                .tipoproceso(tipoProceso)
                .sujetos("test")
                .empleado(empleado)
                .estadoproceso(estadoProceso)
                .build();

        when(processDataService.getProcessById(processid)).thenReturn(proceso);
        ProcessJefeResponse response = processService.getJefeProcess(processid);

        assertEquals("1231233345534", response.getNumeroRadicado());
    }

    @Test
    void deleteProcess() throws ErrorDataServiceException {
        Integer processid = 1;
        when(processDataService.deleteProcess(processid)).thenReturn("Proceso eliminado");

        MessageResponse response = processService.deleteProcess(processid);
        assertEquals("Proceso eliminado", response.getMessage());
    }

    @Test
    void getProcessesByFilter() throws ErrorDataServiceException {
        TipoProceso tipoProceso = TipoProceso.builder()
                .id(1)
                .nombre("test")
                .build();
        Despacho despacho = Despacho.builder()
                .id(1)
                .nombre("test")
                .build();
        Proceso process = Proceso.builder()
                .id(1)
                .radicado("1231233345534")
                .tipoproceso(tipoProceso)
                .despacho(despacho)
                .empleado(Empleado.builder()
                        .usuario(Usuario.builder()
                                .id(1)
                                .nombres("test name").build())
                        .build())
                .firma(new Firma(1, "test firma", "test address"))
                .fecharadicado(LocalDate.now())
                .build();
        List<Actuacion> actuacions = List.of(new Actuacion());
        PageableProcessResponse processResponse = PageableProcessResponse.builder()
                .currentPage(1)
                .totalPages(1)
                .totalItems(1)
                .data(List.of(process))
                .build();
        Integer firmaId = 1;
        when(processDataService.getProcessByFilter(null, firmaId, null, null, null, 0, 10)).thenReturn(processResponse);
        when(actuacionService.findByNoVisto(firmaId)).thenReturn(actuacions).thenReturn(actuacions);

        PageableResponse<ProcessJefeResponse> response = processService.getProcessesByFilter(null, firmaId, null, null, null, 0, 10);
        assertEquals(1, response.getData().size());
    }

    @Test
    void getProcessesByAbogado() throws ErrorDataServiceException {
        TipoProceso tipoProceso = TipoProceso.builder()
                .id(1)
                .nombre("test")
                .build();
        Despacho despacho = Despacho.builder()
                .id(1)
                .nombre("test")
                .build();
        Proceso process = Proceso.builder()
                .id(1)
                .radicado("1231233345534")
                .tipoproceso(tipoProceso)
                .despacho(despacho)
                .fecharadicado(LocalDate.now())
                .build();
        Integer abogadoId = 1;
        PageableProcessResponse processResponse = PageableProcessResponse.builder()
                .currentPage(1)
                .totalPages(1)
                .totalItems(1)
                .data(List.of(process))
                .build();
        when(processDataService.getProcessByAbogado(abogadoId, null, null, null, null, 0, 10)).thenReturn(processResponse);
        PageableResponse<ProcesoResponse> response = processService.getProcessesByAbogado(abogadoId, null, null, null, null, 0, 10);
        assertEquals(1, response.getData().size());
    }

    @Test
    void getProcessAbogado() throws ErrorDataServiceException {
        TipoProceso tipoProceso = TipoProceso.builder()
                .id(1)
                .nombre("test")
                .build();
        Despacho despacho = Despacho.builder()
                .id(1)
                .nombre("test")
                .build();
        EstadoProceso estadoProceso = new EstadoProceso(1, "Activo");
        Set<Audiencia> audiencias = Set.of(Audiencia.builder()
                .id(1)
                .nombre("test")
                .enlace("test.com")
                .build());

        Proceso process = Proceso.builder()
                .id(1)
                .radicado("1231233345534")
                .tipoproceso(tipoProceso)
                .sujetos("test")
                .despacho(despacho)
                .estadoproceso(estadoProceso)
                .fecharadicado(LocalDate.now())
                .build();
        Integer processId = 1;
        when(processDataService.getProcessById(processId)).thenReturn(process);
        when(processDataService.findAllAudienciasByProceso(processId)).thenReturn(audiencias);

        ProcessAbogadoResponse response = processService.getProcessAbogado(processId);
        assertEquals("1231233345534", response.getNumeroRadicado());
        assertDoesNotThrow(() -> processService.getProcessAbogado(processId));

    }

    @Test
    void updateProcess() throws ErrorDataServiceException {
        ProcessUpdateRequest processUpdateRequest = ProcessUpdateRequest.builder()
                .id(1)
                .estadoProceso("Finalizado a favor")
                .idAbogado(3)
                .build();
        Empleado empleado = new Empleado();
        EstadoProceso estadoProceso = new EstadoProceso(1, "Activo");
        Proceso process = new Proceso();
        when(processDataService.getProcessById(1)).thenReturn(process);
        when(firmaService.findEmpleadoByUsuario(processUpdateRequest.getIdAbogado())).thenReturn(empleado);
        when(processDataService.findEstadoProcesoByNombre(processUpdateRequest.getEstadoProceso())).thenReturn(estadoProceso);
        when(processDataService.updateProcess(process)).thenReturn("Proceso actualizado");

        MessageResponse response = processService.updateProcess(processUpdateRequest);
        assertEquals("Proceso actualizado", response.getMessage());
        verify(processDataService, times(1)).updateProcess(process);
    }

    @Test
    void getStateProcessesJefe() throws ErrorDataServiceException {
        List<Proceso> processes = List.of(new Proceso());
        when(processDataService.getStateProcessesJefe("Activo", 1)).thenReturn(processes);
        Map<String, Integer> response = processService.getStateProcessesJefe("Activo", 1);

        assertEquals(1, response.size());
    }

    @Test
    void getStateProcessesAbogado() throws ErrorDataServiceException {
        List<Proceso> processes = List.of(new Proceso());
        when(processDataService.getStateProcessesAbogado("Activo", "danibar")).thenReturn(processes);
        Map<String, Integer> response = processService.getStateProcessesAbogado("Activo", "danibar");

        assertEquals(1, response.size());
    }

    @Test
    void addAudiencia() throws ErrorDataServiceException {
        AudienciaRequest audienciaRequest = AudienciaRequest.builder()
                .nombre("test")
                .enlace("test.com")
                .procesoid(1)
                .build();
        Proceso process = new Proceso();
        when(processDataService.getProcessById(audienciaRequest.getProcesoid())).thenReturn(process);
        when(processDataService.addAudiencia(any())).thenReturn("Audiencia guardada");

        MessageResponse response = processService.addAudiencia(audienciaRequest);
        assertEquals("Audiencia guardada", response.getMessage());
    }

    @Test
    void getEstadoProcesos() throws ErrorDataServiceException {
        List<EstadoProceso> estadoProcesos = List.of(new EstadoProceso());
        when(processDataService.getEstadoProcesos()).thenReturn(estadoProcesos);
        List<EstadoProceso> response = processService.getEstadoProcesos();

        assertEquals(1, response.size());
    }

    @Test
    void updateAudiencia() throws ErrorDataServiceException {
        when(processDataService.updateAudiencia(any(), any())).thenReturn("Audiencia actualizada");
        MessageResponse response = processService.updateAudiencia(1, "test");
        assertEquals("Audiencia actualizada", response.getMessage());
    }

    @Test
    void findAllDespachosWithOutLink() throws ErrorDataServiceException {
        Set<Despacho> despachos = Set.of(new Despacho());
        when(processDataService.findAllDespachosWithOutLink(2023)).thenReturn(despachos);
        Set<Despacho> response = processService.findAllDespachosWithOutLink(2023);

        assertEquals(1, response.size());
    }

    @Test
    void getTipoProcesos() throws ErrorDataServiceException {
        List<TipoProceso> tipoProcesos = List.of(new TipoProceso());
        when(processDataService.getTipoProcesos()).thenReturn(tipoProcesos);
        List<TipoProceso> response = processService.getTipoProcesos();

        assertEquals(1, response.size());
    }

    @Test
    void saveEnlace() throws ErrorDataServiceException {
        Despacho despacho = new Despacho();
        EnlaceRequest enlaceRequest = EnlaceRequest.builder()
                .fechaconsulta(LocalDate.now())
                .url("test.com")
                .despachoid(1)
                .build();
        when(processDataService.findDespachoById(enlaceRequest.getDespachoid())).thenReturn(despacho);
        when(processDataService.saveEnlace(any())).thenReturn("Enlace guardado");

        MessageResponse response = processService.saveEnlace(enlaceRequest);
        assertEquals("Enlace guardado", response.getMessage());
    }

    @Test
    void getProcess() throws ErrorIntegrationServiceException {
        ProcessRequest processRequest = new ProcessRequest();
        String numberProcess = "1231233345534";
        when(processIntegrationService.getProcess(numberProcess)).thenReturn(processRequest);
        ProcessRequest response = processService.getProcess(numberProcess);

        assertNotNull(response);
    }


    @Test
    void getAllProcess() throws ErrorIntegrationServiceException {
        ProcessRequest processRequest = new ProcessRequest();
        String numberProcess = "1231233345534";
        when(processIntegrationService.getAllProcess(numberProcess)).thenReturn(processRequest);
        ProcessRequest response = processService.getAllProcess(numberProcess);

        assertNotNull(response);
    }

    @Test
    void findByRadicado() {
        String radicado = "1231233345534";
        Proceso process = Proceso.builder()
                .radicado(radicado)
                .build();
        try {
            when(processDataService.findByRadicado(radicado)).thenReturn(process);
            processService.findByRadicado(radicado);
        } catch (ErrorDataServiceException e) {
            assertEquals("El proceso con el radicado 1231233345534 ya existe", e.getMessage());
        }

    }

    @Test
    void findByRadicadoNull() throws ErrorDataServiceException {
        String radicado = "123123334553";
        when(processDataService.findByRadicado(radicado)).thenReturn(null);
        assertDoesNotThrow(() -> processService.findByRadicado(radicado));
    }
}