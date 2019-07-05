package com.atstudio.eyfofalafel.backend.domain.files;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "full_path")
    private String fullPath;
    @Transient
    private byte[] content;

}
