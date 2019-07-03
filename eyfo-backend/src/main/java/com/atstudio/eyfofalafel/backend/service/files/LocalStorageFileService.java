package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class LocalStorageFileService implements FileStorageService {

    @Value("files.folder.temp")
    private String tempFileLocation;
    @Value("files.folder.storage")
    private String fileStorageLocation;

    @Override
    public Attachment saveTempFile(Attachment file) {
        return null;
    }
}
