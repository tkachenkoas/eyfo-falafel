package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;

public interface FileStorageService {

    Attachment saveTempFile(Attachment file);

}
