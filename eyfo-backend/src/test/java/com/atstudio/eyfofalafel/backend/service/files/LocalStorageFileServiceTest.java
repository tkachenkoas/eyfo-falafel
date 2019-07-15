package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
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
        String providedRoot = ResourceBundle.getBundle("application").getString("files.drive.folder");
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
    public void tempFileSaved() throws FileNotFoundException {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        assertTrue(attachment != storedTempFile);

        Path filePath = Paths.get(testStorageRoot, storedTempFile.getFullPath());
        assertTrue(Files.exists(filePath));

        underTest.readContent(storedTempFile);
        assertArrayEquals(attachment.getContent(), storedTempFile.getContent());
    }

    @Test
    public void fileMovedToStorage() throws FileNotFoundException {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        Path tempFilePath = Paths.get(testStorageRoot, storedTempFile.getFullPath());
        assertTrue(Files.exists(tempFilePath));

        // Original temp file is removed from temp
        Attachment stored = underTest.saveFileForStorage(storedTempFile);
        underTest.readContent(stored);
        assertArrayEquals(attachment.getContent(), storedTempFile.getContent());
    }

    @Test(expected = FileNotFoundException.class)
    public void storedFileRemoved() throws FileNotFoundException {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        underTest.remove(storedTempFile);
        underTest.readContent(storedTempFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void originalFileRemovedOnMoveToStorage() throws FileNotFoundException {
        Attachment attachment = someAttachment();
        Attachment storedTempFile = underTest.saveTempFile(attachment);

        Attachment stored = underTest.saveFileForStorage(storedTempFile);
        // Original temp file is removed from temp
        underTest.readContent(storedTempFile);
    }

    private Attachment someAttachment() {
        Attachment attachment = new Attachment();
        attachment.setFileName("test.txt");
        attachment.setContent("test-content".getBytes());
        return attachment;
    }

}