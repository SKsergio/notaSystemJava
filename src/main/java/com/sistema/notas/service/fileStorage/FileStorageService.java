package com.sistema.notas.service.fileStorage;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.storage.photo-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar los archivos.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        // vaidar que el archivo no este vacio
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío.");
        }

        // generar nombre y extension del archivo
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";

        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            fileExtension = "";
        }

        // generar un nombre unico para el archivo
        String newName = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(newName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newName;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo " + newName + ". Por favor intente nuevamente!",
                    ex);
        }
    }

    public void delteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            System.err.println("No se pudo eliminar el archivo antiguo: " + fileName);
        }
    }
}
