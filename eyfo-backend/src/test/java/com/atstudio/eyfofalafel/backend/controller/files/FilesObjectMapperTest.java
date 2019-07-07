package com.atstudio.eyfofalafel.backend.controller.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@Slf4j
public class FilesObjectMapperTest {

    FilesObjectMapper underTest = new FilesObjectMapper();

    @Test
    public void restDtoStartsFromPublicAndIsNormalized() {
        Arrays.asList("/temp/someFile.jpg", "temp/someFile.jpg").forEach( (path) -> {
            Attachment attach = new Attachment();
            attach.setFullPath(path);

            FileRestDto restDto = underTest.fromAttachment(attach);
            assertEquals("public/temp/someFile.jpg", restDto.getFullPath());
        });
    }

    @Test
    public void attachmentIsNormalizedAndWithoutPublic() {
        Arrays.asList("/public/temp/someFile.jpg", "public/temp/someFile.jpg").forEach( (path) -> {
            FileRestDto restDto = new FileRestDto();
            restDto.setFullPath(path);

            Attachment attach = underTest.fromRestDto(restDto);
            assertEquals("temp/someFile.jpg", attach.getFullPath());
        });
    }

    @Test
    public void normalizeSlashes() {
        assertEquals(FilesObjectMapper.normalizeSlashes("\\\\temp\\file.name"), "temp/file.name");
        assertEquals(FilesObjectMapper.normalizeSlashes("//temp\\file.name"), "temp/file.name");
    }

    @Test
    public void fileNameTakenFromPath() {
        Arrays.asList("/public/temp/someFile.jpg", "\\public\\temp\\someFile.jpg").forEach( (path) -> {
            FileRestDto restDto = new FileRestDto();
            restDto.setFullPath(path);

            Attachment attach = underTest.fromRestDto(restDto);

            assertEquals("someFile.jpg", attach.getFileName());
        });
    }
}