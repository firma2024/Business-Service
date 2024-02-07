package com.firma.business.service.data;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;
import com.firma.business.service.data.intf.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DataService {

    @Autowired
    private IProcessDataService processService;
    @Autowired
    private IActuacionDataService actuacionService;
    @Autowired
    private IDespachoDataService despachoService;
    @Autowired
    private IEnlaceDataService enlaceService;
    @Autowired
    private IUsuarioDataService usuarioDataService;
    @Autowired
    private IFirmaDataService firmaDataService;
    @Autowired
    private ITipoProcesoDataService tipoProcesoDataService;
    @Autowired
    private IEstadoProcesoDataService estadoProcesoDataService;
    @Autowired
    private IAudienciaDataService audienciaDataService;

    public List<ProcesoResponse> getProcess() throws ErrorDataServiceException {
        return processService.getProcess();
    }

    public String saveActuaciones(List<ActuacionRequest> actuaciones) throws ErrorDataServiceException {
        return actuacionService.saveActuaciones(actuaciones);
    }

    public Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException {
        return actuacionService.findActuacionesNotSend();
    }

    public String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException {
        return actuacionService.updateActuacionesSend(actuaciones);
    }

    public Actuacion getActuacionById(Integer id) throws ErrorDataServiceException {
        return actuacionService.getActuacionById(id);
    }

    public Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorIntegrationServiceException {
        return despachoService.findAllDespachosWithOutLink(year);
    }

    public String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException{
        return enlaceService.saveEnlace(enlaceRequest);
    }

    public String saveProcess(Proceso process) throws ErrorDataServiceException {
        return processService.saveProcess(process);
    }

    public String uploadPhoto(MultipartFile file, Integer usuarioId) throws ErrorDataServiceException, IOException {
        return usuarioDataService.uploadPhoto(file, usuarioId);
    }

    public byte[] downloadPhoto(Integer usuarioId) throws ErrorDataServiceException {
        return usuarioDataService.downloadPhoto(usuarioId);
    }

    public String uploadDocument(MultipartFile file, Integer actuacionId) throws IOException, ErrorDataServiceException {
        return actuacionService.uploadDocument(file, actuacionId);
    }

    public byte[] downloadDocument(Integer actuacionId) throws ErrorDataServiceException {
        return actuacionService.downloadDocument(actuacionId);
    }

    public FileResponse downloadAllDocuments(Integer procesoId) throws ErrorDataServiceException, IOException {
        Set<ActuacionDocumentResponse> documents = actuacionService.downloadAllDocuments(procesoId);

        String radicado = null;
        File tempFolder = new File("temp");
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        File zipFolder = new File("zip");
        if (!zipFolder.exists()) {
            zipFolder.mkdirs();
        }

        for(ActuacionDocumentResponse actuacionDocument : documents) {
            byte[] documentData = actuacionDocument.getDocument();
            File pdfFile = new File(tempFolder, "providencia_" + actuacionDocument.getFechaActuacion() + ".pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            radicado = actuacionDocument.getRadicado();
            fos.write(documentData);
            fos.close();
        }

        // Comprimir los archivos PDF en un archivo ZIP
        File zipFile = new File("zip/providencias_" + radicado + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

        for (File pdfFile : tempFolder.listFiles()) {
            ZipEntry zipEntry = new ZipEntry(pdfFile.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = FileUtils.readFileToByteArray(pdfFile);
            zipOut.write(bytes);

            pdfFile.delete();
        }

        zipOut.close();
        FileUtils.deleteDirectory(tempFolder);

        return FileResponse.builder()
                .file(FileUtils.readFileToByteArray(zipFile))
                .fileName("providencias_" + radicado + ".zip")
                .build();

    }

    public PageableResponse<Proceso> getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        return processService.getProcessByFilter(fechaInicioStr, firmaId, fechaFinStr, estadosProceso, tipoProceso, page, size);
    }

    public PageableResponse<Proceso> getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException {
        return processService.getProcessByAbogado(abogadoId, fechaInicioStr, fechaFinStr, estadosProceso, tipoProceso, page, size);
    }

    public PageableResponse<UsuarioResponse> getAbogadosByFirma(Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException {
        return usuarioDataService.getAbogadosByFirma(firmaId, page, size);
    }

    public PageableResponse<UsuarioResponse> getAbogadosByFilter(List<String> especialidades, Integer numProcesosInicial, Integer numProcesosFinal, Integer page, Integer size) throws ErrorDataServiceException {
        return usuarioDataService.getAbogadosByFilter(especialidades, numProcesosInicial, numProcesosFinal, page, size);
    }

    public PageableResponse<Actuacion> getActuacionesFilter(Integer procesoId, String fechaInicioStr, String fechaFinStr, String estadoActuacion, Integer page, Integer size) throws ErrorDataServiceException {
        return actuacionService.getActuacionesFilter(procesoId, fechaInicioStr, fechaFinStr, estadoActuacion, page, size);
    }

    public PageableResponse<Actuacion> getActuacionesByProcesoAbogado(Integer procesoId, String fechaInicioStr, String fechaFinStr, Boolean existeDoc, Integer page, Integer size) throws ErrorDataServiceException {
        return actuacionService.getActuacionesByProcesoAbogado(procesoId, fechaInicioStr, fechaFinStr, existeDoc, page, size);
    }

    public Firma getFirmaByUser(String userName) throws ErrorDataServiceException {
        return firmaDataService.getFirmaByUser(userName);
    }

    public List<ProcesoResponse> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException {
        return processService.getStateProcessesJefe(state, firmaId);
    }

    public String saveAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        return usuarioDataService.saveAbogado(userRequest);
    }

    public UsuarioResponse getAbogado(Integer abogadoId) throws ErrorDataServiceException {
        return usuarioDataService.getAbogado(abogadoId);
    }

    public List<UsuarioResponse> getAbogadosNames(Integer firmaId) throws ErrorDataServiceException {
        return usuarioDataService.getAbogadosNames(firmaId);
    }

    public ProcesoJefeResponse getProcessById(Integer processId) throws ErrorDataServiceException {
        return processService.getProcessById(processId);
    }

    public List<TipoProceso> getAllTipoProceso() throws ErrorDataServiceException {
        return tipoProcesoDataService.getTipoProcesos();
    }

    public List<EstadoProceso> getAllEstadoProceso() throws ErrorDataServiceException {
        return estadoProcesoDataService.getEstadoProcesos();
    }

    public String deleteProcess(Integer processId) throws ErrorDataServiceException {
        return processService.deleteProcess(processId);
    }

    public String updateProcess(Proceso process) throws ErrorDataServiceException {
        return processService.updateProcess(process);
    }

    public String deleteUser(Integer userId) throws ErrorDataServiceException {
        return usuarioDataService.deleteUser(userId);
    }

    public UsuarioResponse getInfoJefe(Integer id) throws ErrorDataServiceException {
        return usuarioDataService.getInfoJefe(id);
    }

    public String updateInfoJefe(UsuarioRequest userRequest, Integer id) throws ErrorDataServiceException {
        return usuarioDataService.updateInfoJefe(userRequest, id);
    }

    public List<ProcesoResponse> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException {
        return processService.getStateProcessesAbogado(name, userName);
    }

    public UsuarioResponse getUserByUserName(String userName) throws ErrorDataServiceException {
        return usuarioDataService.getUserByUserName(userName);
    }

    public ProcesoAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException {
        return processService.getProcessAbogado(processId);
    }

    public String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException {
        return audienciaDataService.updateAudiencia(id, enlace);
    }

    public String addAudiencia(AudienciaRequest audiencia) throws ErrorDataServiceException {
        return audienciaDataService.addAudiencia(audiencia);
    }

    public UsuarioResponse getInfoAbogado(Integer id) throws ErrorDataServiceException {
        return usuarioDataService.getInfoAbogado(id);
    }

    public String updateInfoAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException {
        return usuarioDataService.updateInfoAbogado(userRequest);
    }
}
