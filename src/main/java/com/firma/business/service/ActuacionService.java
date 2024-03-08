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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ActuacionService {

    @Autowired
    private IProcessDataService processDataService;
    @Autowired
    private IActuacionDataService actuacionDataService;
    @Autowired
    private IActuacionIntegrationService actuacionIntegrationService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Logger loggerService = LoggerFactory.getLogger(ActuacionService.class);

    @Value("${api.presentation.url}")
    private String apiPresentationUrl;

    @Value("${api.estadoactuacion.noVisto}")
    private String estadoActuacionNoVisto;

    @Value("${api.estadoactuacion.visto}")
    private String estadoVisto;



    public MessageResponse saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {
        EstadoActuacion estadoActuacion = actuacionDataService.findEstadoActuacionByName(estadoActuacionNoVisto);
        List<Actuacion> actuacionesList = new ArrayList<>();
        for (ActuacionRequest ac : actuaciones){
            Proceso proceso = processDataService.findByRadicado(ac.getProceso());
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            Actuacion actuacion = Actuacion.builder()
                    .actuacion(ac.getNombreActuacion())
                    .anotacion(ac.getAnotacion())
                    .enviado('N')
                    .fechaactuacion(LocalDateTime.parse(ac.getFechaActuacion(), formatterTime).toLocalDate())
                    .fecharegistro(LocalDateTime.parse(ac.getFechaRegistro(), formatterTime).toLocalDate())
                    .proceso(proceso)
                    .existedoc(ac.isExistDocument())
                    .estadoactuacion(estadoActuacion)
                    .build();

            if (ac.getFechaInicia() != null && ac.getFechaFinaliza() != null){
                actuacion.setFechainicia(LocalDateTime.parse(ac.getFechaInicia(), formatterTime).toLocalDate());
                actuacion.setFechafinaliza(LocalDateTime.parse(ac.getFechaFinaliza(), formatterTime).toLocalDate());
            }
            actuacionesList.add(actuacion);
        }
        return new MessageResponse(actuacionDataService.saveActuaciones(actuacionesList),  null);
    }

    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {
        return actuacionDataService.findActuacionesNotSend();
    }

    public void updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {

        for (Integer actuacionId : actuaciones){
            Actuacion actuacion = actuacionDataService.getActuacion(actuacionId);
            actuacion.setEnviado('Y');
            loggerService.info(actuacionDataService.updateActuacion(actuacion));

            RegistroCorreo reg = RegistroCorreo.builder()
                    .correo(actuacion.getProceso().getEmpleado().getUsuario().getCorreo())
                    .fecha(LocalDateTime.now())
                    .build();

            loggerService.info(actuacionDataService.saveRegistroCorreo(reg));
        }
    }

    public ActuacionResponse getActuacion(Integer id) throws ErrorDataServiceException {
        Actuacion actuacion = actuacionDataService.getActuacion(id);
        String link = null;

        if (actuacion.getExistedoc() && actuacion.getDocumento() == null){
            DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
            String year = yearFormatter.format(actuacion.getFechaactuacion());
            Enlace e = processDataService.findByDespachoAndYear(actuacion.getProceso().getDespacho().getId(), year);
            if (e == null)
                throw new ErrorDataServiceException("No se encontro el enlace para el despacho", 404);
            link = e.getUrl();
        }

        ActuacionResponse res = ActuacionResponse.builder()
                .id(actuacion.getId())
                .sujetos(actuacion.getProceso().getSujetos())
                .despacho(actuacion.getProceso().getDespacho().getNombre())
                .tipoProceso(actuacion.getProceso().getTipoproceso().getNombre())
                .actuacion(actuacion.getActuacion())
                .anotacion(actuacion.getAnotacion())
                .existeDocumento(actuacion.getExistedoc())
                .fechaActuacion(actuacion.getFechaactuacion().format(formatter))
                .fechaRegistro(actuacion.getFecharegistro().format(formatter))
                .link(link)
                .build();

        if (actuacion.getFechainicia() != null && actuacion.getFechafinaliza() != null){
            res.setFechaInicia(actuacion.getFechainicia().format(formatter));
            res.setFechaFinaliza(actuacion.getFechafinaliza().format(formatter));
        }

        return res;
    }

    public PageableResponse<ActuacionJefeResponse> getActuacionesFilter(Integer procesoId, String fechaInicioStr, String fechaFinStr, String estadoActuacion, Integer page, Integer size) throws ErrorDataServiceException {
        PageableActuacionResponse pageableResponse = actuacionDataService.getActuacionesFilter(procesoId, fechaInicioStr, fechaFinStr, estadoActuacion, page, size);
        List<ActuacionJefeResponse> actuacionesResponse = new ArrayList<>();

        for (Actuacion actuacion : pageableResponse.getData()) {
            ActuacionJefeResponse act = ActuacionJefeResponse.builder()
                    .nombreActuacion(actuacion.getActuacion())
                    .fechaActuacion(actuacion.getFechaactuacion().format(formatter))
                    .fechaRegistro(actuacion.getFecharegistro().format(formatter))
                    .anotacion(actuacion.getAnotacion())
                    .existDocument(actuacion.getExistedoc())
                    .estado(actuacion.getEstadoactuacion().getNombre())
                    .build();
            actuacionesResponse.add(act);
        }

        PageableResponse<ActuacionJefeResponse> response = PageableResponse.<ActuacionJefeResponse>builder()
                .data(actuacionesResponse)
                .totalPages(pageableResponse.getTotalPages())
                .totalItems(pageableResponse.getTotalItems())
                .currentPage(pageableResponse.getCurrentPage())
                .build();

        return response;
    }

    public PageableResponse<ActuacionResponse> getActuacionesByProcesoAbogado(Integer procesoId, String fechaInicioStr, String fechaFinStr, Boolean existeDoc, Integer page, Integer size) throws ErrorDataServiceException {
        PageableActuacionResponse pageableResponse = actuacionDataService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size);

        List<ActuacionResponse> actuacionesResponse = new ArrayList<>();
        for (Actuacion actuacion : pageableResponse.getData()){
            ActuacionResponse res = ActuacionResponse.builder()
                    .id(actuacion.getId())
                    .actuacion(actuacion.getActuacion())
                    .anotacion(actuacion.getAnotacion())
                    .existeDocumento(actuacion.getExistedoc())
                    .fechaActuacion(actuacion.getFechaactuacion().format(formatter))
                    .fechaRegistro(actuacion.getFecharegistro().format(formatter))
                    .build();
            actuacionesResponse.add(res);
        }

        PageableResponse<ActuacionResponse> response = PageableResponse.<ActuacionResponse>builder()
                .data(actuacionesResponse)
                .totalPages(pageableResponse.getTotalPages())
                .totalItems(pageableResponse.getTotalItems())
                .currentPage(pageableResponse.getCurrentPage())
                .build();

        return response;
    }

    public List<ActuacionRequest> findNewActuacion(List <FindProcessRequest> process) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.findNewActuacion(process);
    }

    public List<Integer> sendEmailActuacion(List<ActuacionEmailRequest> actuaciones) throws ErrorIntegrationServiceException {
        return actuacionIntegrationService.sendEmailActuacion(actuaciones);
    }

    public EstadoActuacion findEstadoActuacionByName(String state) throws ErrorDataServiceException {
        return actuacionDataService.findEstadoActuacionByName(state);
    }

    public Actuacion findLastActuacion(Integer processid) throws ErrorDataServiceException {
        return actuacionDataService.findLastActuacion(processid);
    }

    public List<ProcesoResponse> getAllProcess() throws ErrorDataServiceException {
        Set<Proceso> process = processDataService.getProcess();
        List<ProcesoResponse> processResponse = new ArrayList<>();

        for (Proceso proceso : process) {

            Actuacion actuacion = this.findLastActuacion(proceso.getId());

            ProcesoResponse p = ProcesoResponse.builder()
                    .numeroProceso(proceso.getNumeroproceso())
                    .numeroRadicado(proceso.getRadicado())
                    .id(proceso.getId())
                    .fechaRadicacion(proceso.getFecharadicado().format(formatter))
                    .fechaUltimaActuacion(actuacion.getFechaactuacion().format(formatter))
                    .build();

            processResponse.add(p);
        }
        return processResponse;
    }

    public List<Actuacion> findByNoVisto(Integer firmaId) throws ErrorDataServiceException {
        return actuacionDataService.findByNoVisto(firmaId);
    }

    public MessageResponse updateActuacion(Integer actionId) throws ErrorDataServiceException {
        Actuacion actuacion = actuacionDataService.getActuacion(actionId);
        EstadoActuacion es = actuacionDataService.findEstadoActuacionByName(estadoVisto);
        actuacion.setEstadoactuacion(es);

        return new MessageResponse(actuacionDataService.updateActuacion(actuacion),  null);
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void findNewActuaciones() {
        try {
            loggerService.info("Buscando actuaciones nuevas");
            List<ProcesoResponse> process = this.getAllProcess();
            List<FindProcessRequest> processFind = new ArrayList<>();

            for (ProcesoResponse procesoResponse : process) {
                FindProcessRequest ac = FindProcessRequest.builder()
                        .number_process(procesoResponse.getNumeroProceso())
                        .file_number(procesoResponse.getNumeroRadicado())
                        .date(procesoResponse.getFechaUltimaActuacion())
                        .build();

                processFind.add(ac);
            }
            List<ActuacionRequest> actuaciones = actuacionIntegrationService.findNewActuacion(processFind);

            if (actuaciones.isEmpty()) {
                loggerService.info("No hay actuaciones nuevas");
                return;
            }

            loggerService.info(this.saveActuaciones(actuaciones).getMessage());

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            loggerService.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/30 7-9 * * ?") //rango de 7:00 am a 9:00 am cada 30 minutos
    public void sendEmailNewActuacion() {
        try {
            loggerService.info("Enviando correos de actuaciones");
            Set<Actuacion> actuaciones = actuacionDataService.findActuacionesNotSend();
            if (actuaciones.isEmpty()) {
                loggerService.info("No hay actuaciones para enviar");
                return;
            }

            List<ActuacionEmailRequest> actuacionesEmail = new ArrayList<>();
            for (Actuacion actuacion : actuaciones) {
                ActuacionEmailRequest actuacionEmail = ActuacionEmailRequest.builder()
                        .id(actuacion.getId())
                        .actuacion(actuacion.getActuacion())
                        .radicado(actuacion.getProceso().getRadicado())
                        .anotacion(actuacion.getAnotacion())
                        .fechaActuacion(actuacion.getFechaactuacion().format(formatter))
                        .emailAbogado(actuacion.getProceso().getEmpleado().getUsuario().getCorreo())
                        .nameAbogado(actuacion.getProceso().getEmpleado().getUsuario().getNombres())
                        .link(String.format("%s/infoaction?id=%d", apiPresentationUrl, actuacion.getId()))
                        .build();

                actuacionesEmail.add(actuacionEmail);
            }
            List<Integer> actSend = actuacionIntegrationService.sendEmailActuacion(actuacionesEmail);

            actuacionDataService.updateActuacionesSend(actSend);

        } catch (ErrorDataServiceException | ErrorIntegrationServiceException e) {
            loggerService.error(e.getMessage());
        }
    }
}
