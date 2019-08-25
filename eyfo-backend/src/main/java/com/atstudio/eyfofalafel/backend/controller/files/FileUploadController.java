package com.atstudio.eyfofalafel.backend.controller.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import com.atstudio.eyfofalafel.backend.service.files.FileStorageService;
import lombok.extern.slf4j.Slf4j;
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

    private FileStorageService fileStorageService;
    private FilesObjectMapper mapper;

    public FileUploadController(FileStorageService fileStorageService, FilesObjectMapper factory) {
        this.fileStorageService = fileStorageService;
        this.mapper = factory;
    }

    @PostMapping("/upload-temp")
    public ResponseEntity<FileRestDto> uploadTempFile(@RequestParam("file") MultipartFile file) throws Exception {
        Attachment tempFile = fileStorageService.saveTempFile(
                mapper.fromMultipart(file)
        );
        return ResponseEntity.ok(
                mapper.fromAttachment(tempFile)
        );
    }

}
