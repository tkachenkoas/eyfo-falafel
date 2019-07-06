package com.atstudio.eyfofalafel.backend.controller.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Component
public class FilesObjectMapper {

    static final String PUBLIC_PREFIX = "public";

    public Attachment fromMultipart(MultipartFile file) throws Exception {
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setContent(file.getBytes());
        return attachment;
    }

    public FileRestDto fromAttachment(Attachment attachment) {
        FileRestDto dto = new FileRestDto();
        dto.setFullPath(Paths.get(PUBLIC_PREFIX, attachment.getFullPath()).normalize().toString());
        dto.setId(attachment.getId());
        return dto;
    }

    public Attachment fromRestDto(FileRestDto restDto) {
        Attachment attachment = new Attachment();
        String normalizedPathWithoutPrefix = Paths.get(restDto.getFullPath().replace(PUBLIC_PREFIX, "")).normalize().toString();
        attachment.setFullPath(normalizedPathWithoutPrefix);
        attachment.setId(restDto.getId());
        return attachment;
    }

}
