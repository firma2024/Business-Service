package com.firma.business.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        // Crear un archivo temporal
        Path tempFile = Files.createTempFile("temp-file", null);

        // Copiar los bytes del MultipartFile al archivo temporal
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Convertir Path a File
        java.io.File convertedFile = tempFile.toFile();

        // Asegurarse de que el archivo se eliminará al finalizar
        convertedFile.deleteOnExit();

        return convertedFile;
    }
}
