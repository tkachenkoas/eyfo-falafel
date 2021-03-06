package com.atstudio.eyfofalafel.backend.controller.files


import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner

import static com.atstudio.eyfofalafel.backend.testutil.TestRequestUtils.*

@RunWith(SpringRunner)
class FileUploadControllerIT {

    @Test
    void fileCanBeViewedAfterTempUpload() throws Exception {
        MockMultipartFile tempFile = testFile()

        def tempUpload = multipart("api/files/upload-temp", tempFile, HashMap)
        byte[] viewTempFile = getFileContent(tempUpload['fullPath'] as String)

        assert viewTempFile == tempFile.getBytes()
    }

    private static MockMultipartFile testFile() {
        return new MockMultipartFile("file", "temp_img.png", "image/png", "some file content".getBytes())
    }

}