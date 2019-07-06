package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
@Slf4j
public class LocalStorageFileService implements FileStorageService {

    public final static String TEMP_FOLDER = "temp";

    @Value("${files.drive.folder}")
    private String fileStorageLocation;

    @PostConstruct
    public void initPaths() {
        try {
            Files.createDirectories(tempPath());
            Files.createDirectories(storagePath());
        } catch (Exception ex) {
            throw new RuntimeException("Could not create file storage directory", ex);
        }
    }

    @Override
    public Attachment saveTempFile(Attachment file) {
        try {
            String targetFileName = String.join(".", getRandomFilename(), getExtension(file.getFileName()).toLowerCase());
            Path targetLocation = tempPath().resolve(targetFileName);
            Files.write(targetLocation, file.getContent());
            file.setFullPath(String.join("/", TEMP_FOLDER, targetFileName).toLowerCase());
            return file;
        } catch (IOException e){
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Attachment saveFileForStorage(Attachment file) {
        if (!file.getFullPath().contains(TEMP_FOLDER)) return file;

        return null;
    }

    @Override
    public void remove(Attachment file) {
        Path filePath = storagePath().resolve(Paths.get(file.getFullPath()));
        if (!Files.exists(filePath)) {
            log.info("File {} does not exist; will do nothing", filePath.toString());
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getRandomFilename() {
        String datePart = new SimpleDateFormat("YYYYMMDDHH").format(new Date());
        String randomPart = String.format("%07d" , new Random().nextInt(1000000));
        return String.join("_", datePart, randomPart);
    }

    private Path tempPath() {
        return Paths.get(fileStorageLocation, TEMP_FOLDER);
    }

    private Path storagePath() {
        return Paths.get(fileStorageLocation);
    }
}
