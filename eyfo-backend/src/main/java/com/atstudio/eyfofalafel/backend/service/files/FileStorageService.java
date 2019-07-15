package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;

import java.io.FileNotFoundException;

public interface FileStorageService {

    Attachment saveTempFile(Attachment file);
    Attachment saveFileForStorage(Attachment file);
    void remove(Attachment file);
    void readContent(Attachment file) throws FileNotFoundException;

}
