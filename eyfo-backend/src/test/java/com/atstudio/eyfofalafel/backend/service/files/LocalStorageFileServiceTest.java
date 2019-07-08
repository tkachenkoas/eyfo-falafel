package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

@Slf4j
public class LocalStorageFileServiceTest {

    private LocalStorageFileService underTest;

    private final String testStorageRoot;

    {
        String providedRoot = ResourceBundle.getBundle("test-config").getString("files.drive.folder");
        testStorageRoot = providedRoot + "tests/";
    }

    @Before
    public void init() {
        underTest = new LocalStorageFileService(testStorageRoot);
        underTest.initPaths();
    }

    @After
    public void clear() {
        try {
            FileUtils.forceDelete(new File(testStorageRoot));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void tempFileSaved() {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        Path filePath = Paths.get(testStorageRoot, storedTempFile.getFullPath());
        assertTrue(Files.exists(filePath));

        byte[] fileContent = {};
        try {
            fileContent = Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        assertArrayEquals(attachment.getContent(), fileContent);
    }

    @Test
    public void storedFileRemoved() {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        Path filePath = Paths.get(testStorageRoot, storedTempFile.getFullPath());
        assertTrue(Files.exists(filePath));

        underTest.remove(storedTempFile);
        assertFalse(Files.exists(filePath));
    }

    @Test
    public void tempFileMovedToStorage() {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        Path tempFilePath = Paths.get(testStorageRoot, storedTempFile.getFullPath());
        assertTrue(Files.exists(tempFilePath));

        // Original temp file is removed from temp
        Attachment stored = underTest.saveFileForStorage(storedTempFile);
        assertFalse(Files.exists(tempFilePath));

        Path storedFilePath = Paths.get(testStorageRoot, stored.getFullPath());
        assertTrue(Files.exists(storedFilePath));

        byte[] storedFileContent = {};
        try {
            storedFileContent = Files.readAllBytes(storedFilePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        assertArrayEquals(attachment.getContent(), storedFileContent);
    }

    private Attachment someAttachment() {
        Attachment attachment = new Attachment();
        attachment.setFileName("test.txt");
        attachment.setContent("test-content".getBytes());
        return attachment;
    }

}