package com.atstudio.eyfofalafel.backend.controller.files

import com.atstudio.eyfofalafel.backend.domain.files.Attachment
import lombok.extern.slf4j.Slf4j
import org.junit.Test

import static com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper.normalizeSlashes

@Slf4j
class FilesObjectMapperTest {

    FilesObjectMapper underTest = new FilesObjectMapper()

    @Test
    void restDtoStartsFromPublicAndIsNormalized() {
        ["/temp/someFile.jpg", "temp/someFile.jpg"].forEach({ path ->
            Attachment attach = new Attachment()
            attach.setFullPath(path)
            FileRestDto restDto = underTest.fromAttachment(attach)
            assert  "public/temp/someFile.jpg" == restDto.getFullPath()
        })
    }

    @Test
    void attachmentIsNormalizedAndWithoutPublic() {
        ["/public/temp/someFile.jpg", "public/temp/someFile.jpg"].forEach({ path ->
            FileRestDto restDto = new FileRestDto()
            restDto.setFullPath(path)

            Attachment attach = underTest.fromRestDto(restDto)
            assert "temp/someFile.jpg" == attach.getFullPath()
        })
    }

    @Test
    void normalizeSlashes() {
        assert  normalizeSlashes("\\\\temp\\file.name") == "temp/file.name"
        assert  normalizeSlashes("//temp\\file.name") == "temp/file.name"
    }

    @Test
    void fileNameTakenFromPath() {
        ["/public/temp/someFile.jpg", "\\public\\temp\\someFile.jpg"].forEach({ path ->
            FileRestDto restDto = new FileRestDto()
            restDto.setFullPath(path)

            Attachment attach = underTest.fromRestDto(restDto)

            assert "someFile.jpg" == attach.getFileName()
        })
    }
}