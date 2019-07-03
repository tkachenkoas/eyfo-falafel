package com.atstudio.eyfofalafel.backend.domain.files;

import lombok.Data;

@Data
public class Attachment {

    private Long id;
    private String fileName;
    private String fullPath;
    private byte[] content;

}
