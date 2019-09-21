package com.atstudio.eyfofalafel.backend.controller.files;

import com.atstudio.eyfofalafel.backend.entities.files.Attachment;
import com.atstudio.eyfofalafel.backend.service.files.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/files/")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final FilesObjectMapper mapper;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService, FilesObjectMapper factory) {
        this.fileStorageService = fileStorageService;
        this.mapper = factory;
    }

    @PostMapping("/upload-temp")
    public ResponseEntity<FileRestDto> uploadTempFile(@RequestParam("file") MultipartFile file) throws Exception {
        Attachment tempFile = fileStorageService.saveTempFile(
                mapper.fromMultipart(file)
        );
        log.info("Uploaded temp file: {}; original size {} kb", tempFile, file.getBytes().length / 1024);
        return ResponseEntity.ok(
                mapper.fromAttachment(tempFile)
        );
    }

}
