package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.*;
import com.firma.business.payload.request.ActuacionEmailRequest;
import com.firma.business.payload.request.ActuacionRequest;
import com.firma.business.payload.request.FindProcessRequest;
import com.firma.business.payload.response.*;
import com.firma.business.intfData.IActuacionDataService;
import com.firma.business.intfData.IProcessDataService;
import com.firma.business.intfIntegration.IActuacionIntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActuacionServiceTest {

    @InjectMocks
    ActuacionService actuacionService;
    @Mock
    IProcessDataService processDataService;
    @Mock
    IActuacionDataService actuacionDataService;
    @Mock
    FirmaService firmaService;
    @Mock
    IActuacionIntegrationService actuacionIntegrationService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveActuacionesWithFechaIniciaAndFechaFinal() throws ErrorDataServiceException {
        EstadoActuacion estadoActuacion = new EstadoActuacion(1, "No Visto");
        ActuacionRequest actuacionRequest = ActuacionRequest.builder()
                .nombreActuacion("test")
                .anotacion("test")
                .proceso("123123123123")
                .fechaActuacion("2023-01-23T00:00:00")
                .existDocument(true)
                .fechaRegistro("2023-01-23T00:00:00")
                .fechaFinaliza("2023-01-23T00:00:00")
                .fechaInicia("2023-01-23T00:00:00")
                .build();
        List<ActuacionRequest> actuacionRequestList = List.of(actuacionRequest);
        Proceso process = Proceso.builder()
                .radicado("123123123123")
                .build();

        when(actuacionDataService.findEstadoActuacionByName("No Visto")).thenReturn(estadoActuacion);
        when(processDataService.findByRadicado("123123123123")).thenReturn(process);
        when(actuacionDataService.saveActuaciones(any())).thenReturn("Actuaciones almacenadas");

        MessageResponse response = actuacionService.saveActuaciones(actuacionRequestList);

        assertEquals("Actuaciones almacenadas", response.getMessage());
    }

    @Test
    void findActuacionesNotSend() throws ErrorDataServiceException {
        Set<Actuacion> actuacionRequestList = Set.of(Actuacion.builder().build());
        when(actuacionDataService.findActuacionesNotSend()).thenReturn(actuacionRequestList);

        Set<Actuacion> response = actuacionService.findActuacionesNotSend();

        assertEquals(actuacionRequestList, response);
    }

    @Test
    void updateActuacionesSend() throws ErrorDataServiceException {
        List<Integer> actuacionIds = List.of(1);
        Usuario user = Usuario.builder()
                .correo("test@test.com")
                .build();
        Empleado employee = Empleado.builder()
                .usuario(user)
                .build();
        Proceso process = Proceso.builder()
                .empleado(employee)
                .build();
        Actuacion actuacion = Actuacion.builder()
                .proceso(process)
                .build();

        when(actuacionDataService.getActuacion(1)).thenReturn(actuacion);
        assertDoesNotThrow(() -> actuacionService.updateActuacionesSend(actuacionIds));
    }

    @Test
    void getActuacion() throws ErrorDataServiceException {
        Integer actuacionId = 2;
        Despacho despacho = Despacho.builder()
                .id(1)
                .build();
        TipoProceso tipoProceso = TipoProceso.builder()
                .nombre("test type")
                .build();
        Proceso process = Proceso.builder()
                .sujetos("test")
                .despacho(despacho)
                .tipoproceso(tipoProceso)
                .build();
        Actuacion actuacion = Actuacion.builder()
                .existedoc(true)
                .documento(null)
                .proceso(process)
                .id(2)
                .actuacion("test")
                .anotacion("my test")
                .fechaactuacion(LocalDate.now())
                .fecharegistro(LocalDate.now())
                .fechainicia(LocalDate.now())
                .fechafinaliza(LocalDate.now())
                .build();
        Enlace en = Enlace.builder()
                .url("test.com")
                .build();
        when(actuacionDataService.getActuacion(actuacionId)).thenReturn(actuacion);
        when(processDataService.findByDespachoAndYear(actuacion.getProceso().getDespacho().getId(), "2024")).thenReturn(en);

        ActuacionResponse actuacionResponse = actuacionService.getActuacion(actuacionId);

        assertEquals(actuacion.getActuacion(), actuacionResponse.getActuacion());
    }

    @Test
    void getActuacionesFilter() throws ErrorDataServiceException {
        EstadoActuacion estadoActuacion = new EstadoActuacion(1, "Visto");
        Actuacion actuacion = Actuacion.builder()
                .anotacion("test")
                .fechaactuacion(LocalDate.now())
                .fecharegistro(LocalDate.now())
                .anotacion("test test")
                .existedoc(true)
                .estadoactuacion(estadoActuacion)
                .build();
        PageableActuacionResponse pageableResponse = PageableActuacionResponse.builder()
                .currentPage(1)
                .totalItems(1)
                .totalPages(1)
                .data(List.of(actuacion))
                .build();

        when(actuacionDataService.getActuacionesFilter(1, "2023-01-23", "2024-01-23", estadoActuacion.getNombre(), 0, 10)).thenReturn(pageableResponse);

        PageableResponse<ActuacionJefeResponse> response = actuacionService.getActuacionesFilter(1, "2023-01-23", "2024-01-23", estadoActuacion.getNombre(), 0, 10);
        assertEquals(response.getData().size(), pageableResponse.getData().size());
    }

    @Test
    void getActuacionesByProcesoAbogado() throws ErrorDataServiceException {
        Boolean existDocument = true;
        Actuacion actuacion = Actuacion.builder()
                .anotacion("test")
                .fechaactuacion(LocalDate.now())
                .fecharegistro(LocalDate.now())
                .anotacion("test test")
                .existedoc(true)
                .build();
        PageableActuacionResponse pageableResponse = PageableActuacionResponse.builder()
                .currentPage(1)
                .totalItems(1)
                .totalPages(1)
                .data(List.of(actuacion))
                .build();

        when(actuacionDataService.getActuacionesByProcesoAbogado(1, "2023-01-23", "2024-01-23", existDocument, 0, 10)).thenReturn(pageableResponse);

        PageableResponse<ActuacionResponse> response = actuacionService.getActuacionesByProcesoAbogado(1, "2023-01-23", "2024-01-23", existDocument, 0, 10);
        assertEquals(response.getData().size(), pageableResponse.getData().size());
    }

    @Test
    void findNewActuacion() throws ErrorIntegrationServiceException {
        List<FindProcessRequest> findProcessRequests = List.of(new FindProcessRequest());
        List<ActuacionRequest> actuacionRequestList = List.of(new ActuacionRequest());
        when(actuacionIntegrationService.findNewActuacion(findProcessRequests)).thenReturn(actuacionRequestList);

        List<ActuacionRequest> actuacionResponse = actuacionService.findNewActuacion(findProcessRequests);

        assertEquals(actuacionRequestList.size(), actuacionResponse.size());
    }

    @Test
    void sendEmailActuacion() throws ErrorIntegrationServiceException {
        List<ActuacionEmailRequest> actuaciones = List.of(new ActuacionEmailRequest());
        List<Integer> actuacionIds = List.of(1);

        when(actuacionIntegrationService.sendEmailActuacion(actuaciones)).thenReturn(actuacionIds);
        List<Integer> idsResponse = actuacionService.sendEmailActuacion(actuaciones);

        assertEquals(idsResponse, actuacionIds);
    }

    @Test
    void findEstadoActuacionByName() throws ErrorDataServiceException {
        EstadoActuacion estadoActuacion = new EstadoActuacion(1, "Estado");
        String state = "Estado";
        when(actuacionDataService.findEstadoActuacionByName(state)).thenReturn(estadoActuacion);

        EstadoActuacion response = actuacionService.findEstadoActuacionByName(state);
        assertEquals(response, estadoActuacion);
    }

    @Test
    void findLastActuacion() throws ErrorDataServiceException {
        Integer processid = 2;
        Actuacion actuacion = new Actuacion();
        when(actuacionDataService.findLastActuacion(processid)).thenReturn(actuacion);

        Actuacion actuacionResponse = actuacionService.findLastActuacion(processid);
        assertEquals(actuacionResponse, actuacion);

    }

    @Test
    void getAllProcess() throws ErrorDataServiceException {
        Actuacion actuacion = Actuacion.builder()
                .fechaactuacion(LocalDate.now())
                .build();

        Proceso process = Proceso.builder()
                .numeroproceso(new BigInteger("131231"))
                .radicado("23423423423422")
                .id(1)
                .fecharadicado(LocalDate.now())
                .build();
        Set<Proceso> processes = Set.of(process);
        when(processDataService.getProcess()).thenReturn(processes);
        when(actuacionDataService.findLastActuacion(process.getId())).thenReturn(actuacion);

        List<ProcesoResponse> responses = actuacionService.getAllProcess();

        assertEquals(responses.size(), processes.size());
    }

    @Test
    void findByNoVisto() throws ErrorDataServiceException {
        Integer firmaId = 2;
        List<Actuacion> actuacions = List.of(new Actuacion());
        when(actuacionDataService.findByNoVisto(firmaId)).thenReturn(actuacions);

        List<Actuacion> response = actuacionService.findByNoVisto(firmaId);

        assertEquals(response.size(), actuacions.size());
    }

    @Test
    void updateActuacion() throws ErrorDataServiceException {
        Integer actuacionId = 3;
        EstadoActuacion es = new EstadoActuacion(1, "Visto");
        Actuacion ac = new Actuacion();

        when(actuacionDataService.getActuacion(actuacionId)).thenReturn(ac);
        when(actuacionDataService.findEstadoActuacionByName("Visto")).thenReturn(es);
        when(actuacionDataService.updateActuacion(ac)).thenReturn("Actuacion actualizada");

        MessageResponse response = actuacionService.updateActuacion(actuacionId);
        assertDoesNotThrow(() -> actuacionService.updateActuacion(actuacionId));
        assertEquals("Actuacion actualizada", response.getMessage());

    }
}