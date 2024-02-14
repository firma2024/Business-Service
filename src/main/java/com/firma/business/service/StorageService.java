package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.response.ActuacionDocumentResponse;
import com.firma.business.payload.response.FileResponse;
import com.firma.business.service.data.intf.IStorageDataService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class StorageService {

    @Autowired
    private IStorageDataService storageDataService;

    public String uploadPhoto(MultipartFile file, Integer usuarioId) throws IOException, ErrorDataServiceException {
        return storageDataService.uploadPhoto(file, usuarioId);
    }

    public byte [] downloadPhoto(Integer usuarioId) throws ErrorDataServiceException {
        return storageDataService.downloadPhoto(usuarioId);
    }

    public String uploadDocument(MultipartFile file, Integer actuacionId) throws ErrorDataServiceException, IOException {
        return storageDataService.uploadDocument(file, actuacionId);
    }

    public byte [] downloadDocument(Integer actuacionId) throws ErrorDataServiceException {
        return storageDataService.downloadDocument(actuacionId);
    }

    public FileResponse downloadAllDocuments (Integer processId) throws ErrorDataServiceException, IOException {
        Set<ActuacionDocumentResponse> documents = storageDataService.downloadAllDocuments(processId);
        if (documents.isEmpty()) {
            throw new ErrorDataServiceException("No se encontraron documentos");
        }

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
