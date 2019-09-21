package com.atstudio.eyfofalafel.backend.service.files

import com.atstudio.eyfofalafel.backend.entities.files.Attachment
import lombok.extern.slf4j.Slf4j
import org.apache.commons.io.FileUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.core.io.ClassPathResource

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.apache.commons.io.IOUtils.toByteArray
import static org.junit.Assert.assertArrayEquals
import static org.junit.Assert.assertTrue

@Slf4j
class LocalStorageFileServiceTest {

    private LocalStorageFileService underTest

    private final String testStorageRoot

    {
        String providedRoot = ResourceBundle.getBundle("application").getString("files.drive.folder")
        testStorageRoot = providedRoot + "tests/"
    }

    @Before
    void init() {
        underTest = new LocalStorageFileService(testStorageRoot)
        underTest.initPaths()
    }

    @After
    void clear() {
        try {
            FileUtils.forceDelete(new File(testStorageRoot))
        } catch (IOException e) {
            log.error(e.getMessage(), e)
        }
    }

    @Test
    void tempFileSaved() throws FileNotFoundException {
        Attachment attachment = someAttachment()
        Attachment storedTempFile = underTest.saveTempFile(attachment)

        assert !attachment.is(storedTempFile)

        Path filePath = Paths.get(testStorageRoot, storedTempFile.getFullPath())
        assert Files.exists(filePath)

        underTest.readContent(storedTempFile)
        assertArrayEquals(attachment.getContent(), storedTempFile.getContent())
    }

    @Test
    void tempFileResized() throws FileNotFoundException {
        Attachment storedTempFile = underTest.saveTempFile(
                someAttachment(
                        toByteArray(new ClassPathResource("test_img.jpg").getInputStream() as InputStream),
                        "test_img.jpg"
                )
        )

        underTest.readContent(storedTempFile)
        assert storedTempFile.getContent().length < 500_000
    }

    @Test
    void fileMovedToStorage() throws FileNotFoundException {
        Attachment attachment = someAttachment()
        Attachment storedTempFile = underTest.saveTempFile(attachment)

        Path tempFilePath = Paths.get(testStorageRoot, storedTempFile.getFullPath())
        assertTrue(Files.exists(tempFilePath))

        // Original temp file is removed from temp
        Attachment stored = underTest.saveFileForStorage(storedTempFile)
        underTest.readContent(stored)
        assertArrayEquals(attachment.getContent(), storedTempFile.getContent())
    }

    @Test(expected = FileNotFoundException.class)
    void storedFileRemoved() throws FileNotFoundException {
        Attachment attachment = someAttachment()
        Attachment storedTempFile = underTest.saveTempFile(attachment)

        underTest.remove(storedTempFile)
        underTest.readContent(storedTempFile)
    }

    @Test(expected = FileNotFoundException.class)
    void originalFileRemovedOnMoveToStorage() throws FileNotFoundException {
        Attachment attachment = someAttachment()
        Attachment storedTempFile = underTest.saveTempFile(attachment)

        Attachment stored = underTest.saveFileForStorage(storedTempFile)
        // Original temp file is removed from temp
        underTest.readContent(storedTempFile)
    }

    private static Attachment someAttachment(byte[] content = "test-content".getBytes(), String name = "test.txt") {
        Attachment attachment = new Attachment()
        attachment.setFileName(name)
        attachment.setContent(content)
        return attachment
    }

}