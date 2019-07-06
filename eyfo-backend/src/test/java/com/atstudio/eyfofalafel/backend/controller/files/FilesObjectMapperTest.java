package com.atstudio.eyfofalafel.backend.controller.files;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;

import static com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper.PUBLIC_PREFIX;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class FilesObjectMapperTest {

    FilesObjectMapper underTest = new FilesObjectMapper();

    @Test
    public void restDtoStartsFromPublicAndIsNormalized() {
        Arrays.asList("/temp/someFile.jpg", "temp/someFile.jpg").forEach( (path) -> {
            Attachment attach = new Attachment();
            attach.setFullPath(path);

            FileRestDto restDto = underTest.fromAttachment(attach);

            String restPath = restDto.getFullPath();

            assertTrue(isNormalized(restPath));
            assertTrue(restPath.startsWith(PUBLIC_PREFIX));
            assertFalse(restPath.startsWith("/"));
        });
    }

    @Test
    public void attachmentIsNormalizedAndWithoutPublic() {
        Arrays.asList("/public/temp/someFile.jpg", "public/temp/someFile.jpg").forEach( (path) -> {
            FileRestDto restDto = new FileRestDto();
            restDto.setFullPath(path);

            Attachment attach = underTest.fromRestDto(restDto);

            String attachPath = attach.getFullPath();
            assertTrue(isNormalized(attachPath));
            assertFalse(attachPath.contains(PUBLIC_PREFIX));
        });
    }

    private boolean isNormalized(String path) {
        return path.equals(Paths.get(path).normalize().toString());
    }
}