package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto;
import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FilesObjectFactory {

    public Attachment fromMultipart(MultipartFile file) throws Exception {
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setContent(file.getBytes());
        return attachment;
    }

    public FileRestDto fromAttachment(Attachment attachment) {
        FileRestDto dto = new FileRestDto();
        dto.setFullPath(attachment.getFullPath());
        dto.setId(attachment.getId());
        return dto;
    }

}
