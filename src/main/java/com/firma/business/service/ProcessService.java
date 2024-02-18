package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.model.*;
import com.firma.business.payload.request.*;
import com.firma.business.payload.response.*;
import com.firma.business.service.data.intf.IProcessDataService;
import com.firma.business.service.integration.intf.IProcessIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProcessService {

    @Autowired
    private IProcessDataService processDataService;
    @Autowired
    private IProcessIntegrationService processIntegrationService;
    @Autowired
    private ActuacionService actuacionService;
    @Autowired
    private FirmaService firmaService;
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Logger logger = LoggerFactory.getLogger(ProcessService.class);


    public String saveProcess(ProcessRequest processRequest) throws ErrorDataServiceException {

        Empleado empleado = firmaService.findEmpleadoByUsuario(processRequest.getIdAbogado());

        TipoProceso tipoProceso = processDataService.findTipoProcesoByNombre(processRequest.getTipoProceso());
        if (tipoProceso == null) {
            tipoProceso = TipoProceso.builder()
                    .nombre(processRequest.getTipoProceso())
                    .build();
        }

        Despacho despacho = processDataService.findDespachoByNombre(processRequest.getDespacho());
        if (despacho == null) {
            despacho = Despacho.builder()
                    .nombre(processRequest.getDespacho())
                    .build();
        }

        LocalDateTime dateRadicado = LocalDateTime.parse(processRequest.getFechaRadicacion(), formatterTime);

        EstadoProceso estadoProceso = processDataService.findEstadoProcesoByNombre("Activo");

        Proceso newProceso = Proceso.builder()
                .radicado(processRequest.getNumeroRadicado())
                .numeroproceso(processRequest.getIdProceso())
                .sujetos(processRequest.getSujetos())
                .fecharadicado(dateRadicado.toLocalDate())
                .ubicacionexpediente(processRequest.getUbicacionExpediente())
                .eliminado('N')
                .despacho(despacho)
                .tipoproceso(tipoProceso)
                .estadoproceso(estadoProceso)
                .empleado(empleado)
                .firma(empleado.getFirma())
                .build();

        EstadoActuacion estadoActuacion = actuacionService.findEstadoActuacionByName("Visto");

        List<Actuacion> actuaciones = new ArrayList<>();

        for (ActuacionRequest actuacionReq : processRequest.getActuaciones()) {
            LocalDateTime dateActuacion = LocalDateTime.parse(actuacionReq.getFechaActuacion(), formatterTime);
            LocalDateTime dateRegistro = LocalDateTime.parse(actuacionReq.getFechaRegistro(), formatterTime);

            Actuacion newActuacion = Actuacion.builder()
                    .anotacion(actuacionReq.getAnotacion())
                    .actuacion(actuacionReq.getNombreActuacion())
                    .estadoactuacion(estadoActuacion)
                    .fechaactuacion(dateActuacion.toLocalDate())
                    .fecharegistro(dateRegistro.toLocalDate())
                    .documento(null)
                    .enviado('Y')
                    .existedoc(actuacionReq.isExistDocument())
                    .build();

            if (actuacionReq.getFechaInicia() != null && actuacionReq.getFechaFinaliza() != null) {
                LocalDateTime dateInicia = LocalDateTime.parse(actuacionReq.getFechaInicia(), formatterTime);
                LocalDateTime dateFinaliza = LocalDateTime.parse(actuacionReq.getFechaFinaliza(), formatterTime);
                newActuacion.setFechainicia(dateInicia.toLocalDate());
                newActuacion.setFechafinaliza(dateFinaliza.toLocalDate());
            }

            actuaciones.add(newActuacion);
        }

        ProcessDataRequest processDataRequest = ProcessDataRequest.builder()
                .process(newProceso)
                .actions(actuaciones)
                .build();

        return processDataService.saveProcess(processDataRequest);

    }

    public ProcessJefeResponse getJefeProcess(Integer processId) throws ErrorDataServiceException {
        Proceso process = processDataService.getProcessById(processId);

        return ProcessJefeResponse.builder()
                .id(process.getId())
                .numeroRadicado(process.getRadicado())
                .tipoProceso(process.getTipoproceso().getNombre())
                .sujetos(process.getSujetos())
                .abogado(process.getEmpleado().getUsuario().getNombres())
                .estado(process.getEstadoproceso().getNombre())
                .build();
    }

    public String deleteProcess(Integer processId) throws ErrorDataServiceException {
        return processDataService.deleteProcess(processId);
    }

    public PageableResponse<ProcessJefeResponse> getProcessesByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        PageableProcessResponse pageableResponse = processDataService.getProcessByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size);

        List<ProcessJefeResponse> responses = new ArrayList<>();

        for (Proceso proceso : pageableResponse.getData()) {
            boolean estado = false;
            List<Actuacion> actuaciones = actuacionService.findByNoVisto(proceso.getFirma().getId());
            if (!actuaciones.isEmpty()) {
                estado = true;
            }
            ProcessJefeResponse response = ProcessJefeResponse.builder()
                    .id(proceso.getId())
                    .numeroRadicado(proceso.getRadicado())
                    .tipoProceso(proceso.getTipoproceso().getNombre())
                    .despacho(proceso.getDespacho().getNombre())
                    .abogado(proceso.getEmpleado().getUsuario().getNombres())
                    .fechaRadicacion(proceso.getFecharadicado().format(formatter))
                    .estadoVisto(estado)
                    .build();
            responses.add(response);
        }
        PageableResponse<ProcessJefeResponse> response = PageableResponse.<ProcessJefeResponse>builder()
                .data(responses)
                .currentPage(pageableResponse.getCurrentPage())
                .totalItems(pageableResponse.getTotalItems())
                .totalPages(pageableResponse.getTotalPages())
                .build();

        return response;
    }
    public PageableResponse<ProcesoResponse> getProcessesByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        PageableProcessResponse pageableResponse = processDataService.getProcessByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size);
        List<ProcesoResponse> responses = new ArrayList<>();

        for (Proceso proceso : pageableResponse.getData()) {
            ProcesoResponse response = ProcesoResponse.builder()
                    .id(proceso.getId())
                    .numeroRadicado(proceso.getRadicado())
                    .despacho(proceso.getDespacho().getNombre())
                    .tipoProceso(proceso.getTipoproceso().getNombre())
                    .fechaRadicacion(proceso.getFecharadicado().format(formatter))
                    .build();
            responses.add(response);
        }

        PageableResponse<ProcesoResponse> response = PageableResponse.<ProcesoResponse>builder()
                .data(responses)
                .totalPages(pageableResponse.getTotalPages())
                .totalItems(pageableResponse.getTotalItems())
                .currentPage(pageableResponse.getCurrentPage())
                .build();

        return response;
    }

    public ProcessAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException {
        Proceso process = processDataService.getProcessById(processId);
        Set<Audiencia> audiencias = processDataService.findAllAudienciasByProceso(processId);
        List<AudienciaResponse> audienciasResponse = new ArrayList<>();

        for (Audiencia audiencia : audiencias) {
            audienciasResponse.add(AudienciaResponse.builder()
                    .id(audiencia.getId())
                    .nombre(audiencia.getNombre())
                    .enlace(audiencia.getEnlace())
                    .build());
        }

        return ProcessAbogadoResponse.builder()
                .id(process.getId())
                .numeroRadicado(process.getRadicado())
                .tipoProceso(process.getTipoproceso().getNombre())
                .sujetos(process.getSujetos())
                .despacho(process.getDespacho().getNombre())
                .fechaRadicacion(process.getFecharadicado().format(formatter))
                .estado(process.getEstadoproceso().getNombre())
                .audiencias(audienciasResponse)
                .build();
    }

    public String updateProcess(ProcessUpdateRequest processRequest) throws ErrorDataServiceException {
        Proceso process = processDataService.getProcessById(processRequest.getId());

        if (processRequest.getIdAbogado() != null) {
            Empleado employee = firmaService.findEmpleadoByUsuario(processRequest.getIdAbogado());
            process.setEmpleado(employee);
        }

        if (processRequest.getEstadoProceso() != null){
            EstadoProceso estadoProceso = processDataService.findEstadoProcesoByNombre(processRequest.getEstadoProceso());
            process.setEstadoproceso(estadoProceso);
        }

        return processDataService.updateProcess(process);
    }


    public  Map<String, Integer> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException {
        List<Proceso> processes = processDataService.getStateProcessesJefe(state, firmaId);

        return Map.of(
                "value", processes.size()
        );
    }

    public Map<String, Integer> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException {
        List<Proceso> processes = processDataService.getStateProcessesAbogado(name, userName);
        return Map.of(
                "value", processes.size()
        );
    }

    public String addAudiencia(AudienciaRequest audiencia) throws ErrorDataServiceException {
        Proceso process = processDataService.getProcessById(audiencia.getProcesoid());
        Audiencia newAudiencia = Audiencia.builder()
                .enlace(audiencia.getEnlace())
                .nombre(audiencia.getNombre())
                .proceso(process)
                .build();

        return processDataService.addAudiencia(newAudiencia);
    }


    public List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException {
        return processDataService.getEstadoProcesos();
    }

    public String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException {
        return processDataService.updateAudiencia(id, enlace);
    }

    public Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorDataServiceException {
        return processDataService.findAllDespachosWithOutLink(year);
    }

    public List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException {
        return processDataService.getTipoProcesos();
    }

    public String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException {
        Despacho despacho = processDataService.findDespachoById(enlaceRequest.getDespachoid());
        Enlace enlace = Enlace.builder()
                .url(enlaceRequest.getUrl())
                .fechaconsulta(enlaceRequest.getFechaconsulta())
                .despacho(despacho)
                .build();

        return processDataService.saveEnlace(enlace);
    }

    public ProcessRequest getProcess(String numberProcess) throws ErrorIntegrationServiceException {
        return processIntegrationService.getProcess(numberProcess);
    }



    public ProcessRequest getAllProcess(String numberProcess) throws ErrorIntegrationServiceException {
        return processIntegrationService.getAllProcess(numberProcess);
    }

    public void findByRadicado(String numeroRadicado) throws ErrorDataServiceException {
        Proceso p = processDataService.findByRadicado(numeroRadicado);
        if (p != null) {
            throw new ErrorDataServiceException(String.format("El proceso con el radicado %s ya existe", p.getRadicado()));
        }
    }

    @Scheduled(fixedRate = 600000)
    public void updateDespachoEnlace(){
        try {
            logger.info("Buscando enlaces de despachos");
            Set<DespachoFecha> despachosFecha = processDataService.findAllDespachosWithDateActuacion();
            for(DespachoFecha despachoFecha : despachosFecha){
                Enlace enlace = processDataService.findByDespachoAndYear(despachoFecha.getDespachoId(), String.valueOf(despachoFecha.getYear()));
                if (enlace == null){
                    logger.info(despachoFecha.getNombre());
                    DespachoResponse url = processIntegrationService.findUrlDespacho(despachoFecha.getNombre(), despachoFecha.getYear());
                    EnlaceRequest en = EnlaceRequest.builder()
                            .url(url.getUrl_despacho())
                            .despachoid(despachoFecha.getDespachoId())
                            .fechaconsulta(LocalDate.now().withYear(despachoFecha.getYear()))
                            .build();
                    logger.info(this.saveEnlace(en));
                }
            }
        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            logger.error(e.getMessage());
        }
    }
}
