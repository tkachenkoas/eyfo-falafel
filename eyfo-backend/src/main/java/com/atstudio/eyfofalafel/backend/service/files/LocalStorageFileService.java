package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper.normalizeSlashes;
import static java.net.URLConnection.guessContentTypeFromName;
import static java.net.URLConnection.guessContentTypeFromStream;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.contains;

@Slf4j
public class LocalStorageFileService implements FileStorageService {

    public final static String TEMP_FOLDER = "temp";
    private String fileStorageLocation;

    public static final long MAX_IMAGE_SIZE = 500_000;

    public LocalStorageFileService(String fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
    }

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
        String targetFileName = String.join(".", getRandomFilename(), getExtension(file.getFileName()).toLowerCase());

        Path targetLocation = tempPath().resolve(targetFileName);
        if (isImage(file)) {
            writeResizedImageToFile(file.getContent(), targetLocation);
        } else {
            writeContentToFile(file.getContent(), targetLocation);
        }

        Attachment result = Attachment.newFrom(file);
        result.setFullPath(String.join("/", TEMP_FOLDER, targetFileName).toLowerCase());
        return result;
    }

    @SneakyThrows
    private void writeContentToFile(byte[] content, Path targetLocation) {
        Files.write(targetLocation, content);
    }

    @SneakyThrows
    private boolean isImage(Attachment file) {
        String contentType = file.getContent() != null
                ? guessContentTypeFromStream(new ByteArrayInputStream(file.getContent()))
                : guessContentTypeFromName(file.getFileName());

        return contains(contentType,"image");
    }

    @SneakyThrows
    private void writeResizedImageToFile(byte[] content, Path targetLocation) {
        if (content.length < MAX_IMAGE_SIZE) {
            writeContentToFile(content, targetLocation);
        }
        BufferedImage srcImage = ImageIO.read(new ByteArrayInputStream(content));

        double scaleFactor = Math.sqrt(content.length / MAX_IMAGE_SIZE);

        int trgtHeight = (int) (srcImage.getHeight() / scaleFactor);
        int trgtWidth = (int) (srcImage.getWidth() / scaleFactor);

        // creates output image
        BufferedImage outputImage = new BufferedImage(trgtWidth, trgtHeight, srcImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(srcImage, 0, 0, trgtWidth, trgtHeight, null);
        g2d.dispose();

        String formatName = getExtension(targetLocation.toFile().getName()).toLowerCase();
        // writes to output file
        ImageIO.write(outputImage, formatName, targetLocation.toFile());
    }

    @Override
    public Attachment saveFileForStorage(Attachment attachmentToStore) {
        if (!attachmentToStore.getFullPath().contains(TEMP_FOLDER)) return attachmentToStore;
        Attachment stored = Attachment.newFrom(attachmentToStore);

        String tempPath = attachmentToStore.getFullPath();
        File original = Paths.get(fileStorageLocation, tempPath).toFile();
        if (!original.exists()) {
            return null;
        }
        File target = Paths.get(fileStorageLocation, tempPath.replace(TEMP_FOLDER, "")).toFile();
        try {
            FileUtils.copyFile(original, target);
            removeByPath(original.toPath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        String relativePath = normalizeSlashes(target.getPath().replace(storagePath().toString(),""));
        stored.setFullPath(relativePath);
        return stored;
    }

    @Override
    public void remove(Attachment file) {
        Path filePath = storagePath().resolve(Paths.get(file.getFullPath()));
        removeByPath(filePath);
    }

    private void removeByPath(Path filePath) {
        if (!Files.exists(filePath)) {
            log.info("File {} does not exist; will do nothing", filePath.toString());
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void readContent(Attachment file) throws FileNotFoundException {
        try {
            byte[] fileContent = Files.readAllBytes(storagePath().resolve(Paths.get(file.getFullPath())));
            file.setContent(fileContent);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileNotFoundException(file.getFullPath());
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
