package com.atstudio.eyfofalafel.backend.entities.files;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

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

    public static Attachment newFrom(Attachment src) {
        Attachment result = new Attachment();
        result.setFileName(src.getFileName());
        result.setFullPath(src.getFullPath());
        result.setContent(src.getContent());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;

        Attachment that = (Attachment) o;
        return Objects.equals(this.id, that.id) ||
                Objects.equals(this.fullPath, that.fullPath);
    }

    @Override
    public int hashCode() {
        return fullPath.hashCode();
    }
}
