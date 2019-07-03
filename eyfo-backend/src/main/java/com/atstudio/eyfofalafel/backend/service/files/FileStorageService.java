package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;

import java.io.IOException;

public interface FileStorageService {

    Attachment saveTempFile(Attachment file) throws IOException;

}
