package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.ActuacionDocumentResponse;
import com.firma.business.payload.FileResponse;
import com.firma.business.service.data.intf.IStorageDataService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<?> uploadPhoto(MultipartFile file, Integer usuarioId){
        try {
            return new ResponseEntity<>(storageDataService.uploadPhoto(file, usuarioId), HttpStatus.CREATED);
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> downloadPhoto(Integer usuarioId){
        try {
            byte [] response = storageDataService.downloadPhoto(usuarioId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> uploadDocument(MultipartFile file, Integer actuacionId){
        try {
            return new ResponseEntity<>(storageDataService.uploadDocument(file, actuacionId), HttpStatus.CREATED);
        } catch (IOException | ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> downloadDocument(Integer actuacionId){
        try {
            byte [] response = storageDataService.downloadDocument(actuacionId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response);
        } catch (ErrorDataServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public FileResponse downloadAllDocuments (Integer processId) throws ErrorDataServiceException, IOException {
        Set<ActuacionDocumentResponse> documents = storageDataService.downloadAllDocuments(processId);

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
