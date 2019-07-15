package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.TestContextConfig;
import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestContextConfig.class)
@AutoConfigureMockMvc(secure = false)
public class PlaceControllerIT {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    @SqlGroup({
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    })
    public void newPlace() throws Exception {
        PlaceRestDto newPlace = testPlace();

        String placeCreateResponse = this.mockMvc.perform(post("/api/places/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(newPlace)))
                            .andExpect(status().isOk())
                            .andReturn().getResponse().getContentAsString();
        PlaceRestDto result = mapper.readValue(placeCreateResponse, PlaceRestDto.class);

        assertEquals(newPlace, result);

        String allPlaces = this.mockMvc.perform(get("/api/places/"))
                                    .andExpect(status().isOk())
                                    .andReturn().getResponse()
                                    .getContentAsString();
        List<PlaceRestDto> places = Lists.newArrayList(mapper.readValue(allPlaces, PlaceRestDto[].class));
        assertTrue(places.size() == 1);
        assertEquals(newPlace, places.get(0));
    }

    @Test
    public void fileCanBeViewedAfterTempUpload() throws Exception {
        MockMultipartFile tempFile = testFile();

        FileRestDto tempUpload = mapper.readValue(this.mockMvc
                                        .perform(multipart("/api/files/upload-temp")
                                                .file(tempFile))
                                        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
                                        FileRestDto.class);

        String viewTempFile = this.mockMvc.perform(get("/" + tempUpload.getFullPath()))
                            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertArrayEquals(viewTempFile.getBytes(), tempFile.getBytes());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    })
    public void attachmentCanBeViewedAfterPlaceCreate() throws Exception {
        MockMultipartFile tempFile = testFile();

        FileRestDto tempUpload = mapper.readValue(this.mockMvc
                        .perform(multipart("/api/files/upload-temp")
                                .file(tempFile))
                        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
                FileRestDto.class);

        PlaceRestDto newPlace = testPlace();
        newPlace.setAttachments(Lists.newArrayList(tempUpload));

        PlaceRestDto savedPlace = mapper.readValue(this.mockMvc
                        .perform(post("/api/places/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(newPlace)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                PlaceRestDto.class);

        String viewStoredFile = this.mockMvc.perform(get("/" + savedPlace.getAttachments().get(0).getFullPath()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertArrayEquals(viewStoredFile.getBytes(), tempFile.getBytes());
    }

    private MockMultipartFile testFile() {
        return new MockMultipartFile("file", "temp_img.png", "image/png", "some file content".getBytes());
    }

    private PlaceRestDto testPlace() {
        PlaceRestDto place = new PlaceRestDto();
        place.setName("Test place");
        place.setPriceFrom(BigDecimal.ONE);
        place.setPriceTo(BigDecimal.TEN);
        place.setDescription("Some test description");
        LocationRestDTO location = new LocationRestDTO();
        location.setLatitude(BigDecimal.ONE);
        location.setLongitude(BigDecimal.TEN);
        location.setAddress("Test street, 5");
        place.setLocation(location);
        return place;
    }

}