package com.atstudio.eyfofalafel.backend.service.files;

import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto;
import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FilesObjectFactory {

    public static final String PUBLIC_PREFIX = "/public/";

    public Attachment fromMultipart(MultipartFile file) throws Exception {
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setContent(file.getBytes());
        return attachment;
    }

    public FileRestDto fromAttachment(Attachment attachment) {
        FileRestDto dto = new FileRestDto();
        dto.setFullPath(PUBLIC_PREFIX + attachment.getFullPath());
        dto.setId(attachment.getId());
        return dto;
    }

    public Attachment fromRestDto(FileRestDto restDto) {
        Attachment attachment = new Attachment();
        attachment.setFullPath(restDto.getFullPath().replace(PUBLIC_PREFIX, ""));
        attachment.setId(restDto.getId());
        return attachment;
    }

}
