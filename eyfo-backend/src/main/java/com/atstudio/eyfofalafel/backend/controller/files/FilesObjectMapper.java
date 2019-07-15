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
        String fullPath = normalizeSlashes(Paths.get(PUBLIC_PREFIX, attachment.getFullPath()).toString());
        dto.setFullPath(fullPath);
        dto.setId(attachment.getId());
        return dto;
    }

    public Attachment fromRestDto(FileRestDto restDto) {
        Attachment attachment = new Attachment();
        String normalizedPathWithoutPrefix = normalizeSlashes(restDto.getFullPath().replace(PUBLIC_PREFIX, ""));
        attachment.setFullPath(normalizedPathWithoutPrefix);
        String[] path = normalizeSlashes(restDto.getFullPath()).split("/");
        String fileName = path[path.length-1];
        attachment.setFileName(fileName);
        attachment.setId(restDto.getId());
        return attachment;
    }

    public static String normalizeSlashes(String path) {
        return path.replaceAll("\\\\+","/")
                   .replaceAll("/+","/")
                   .replaceAll("^/","");
    }

}
