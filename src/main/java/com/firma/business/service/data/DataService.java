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
import java.io.FileNotFoundException;
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
}
