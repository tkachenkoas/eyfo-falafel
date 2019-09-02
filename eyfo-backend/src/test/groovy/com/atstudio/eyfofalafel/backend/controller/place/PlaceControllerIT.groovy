package com.atstudio.eyfofalafel.backend.controller.place

import com.atstudio.eyfofalafel.backend.TestDataSourceAutoConfiguration
import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.annotation.Import
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner

import static com.atstudio.eyfofalafel.backend.testutil.TestRequestUtils.*
import static com.jayway.restassured.RestAssured.given
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD

@RunWith(SpringRunner)
@Import(TestDataSourceAutoConfiguration)
class PlaceControllerIT {

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void newPlace() throws Exception {
        PlaceRestDto newPlace = testPlace()

        PlaceRestDto result = performPost("api/places/new", newPlace, PlaceRestDto)

        assert newPlace == result
        List<PlaceRestDto> places = getPlaces()

        assert places.size() == 1
        assert newPlace == places.get(0)
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void simpleSearchTest() throws Exception {
        List<PlaceRestDto> places = getPlaces(
                ["searchText" : "фалафельная"] as Map
        )
        assert places.size() == 1
        assert places[0].getName().contains('фалафельная')
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void findByPaging() throws Exception {
        def pagingTest = { pageNum, String text ->
            List<PlaceRestDto> places = getPlaces(
                    [
                            "pageNumber" : pageNum,
                            "pageSize": 1
                    ] as Map
            )
            assert places.size() == 1
            assert places[0].getName().contains(text)
        }

        pagingTest(0, 'фалафельная')
        pagingTest(1, 'шаурмяшная')

    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void findNearbyPlaces() {

        // first, let's create two places not far from each other with same longidute but different latitude
        def testPlace = testPlace()

        testPlace.location.latitude = testPlace.location.latitude + 0.1
        performPost("api/places/new", testPlace, PlaceRestDto)

        testPlace.location.latitude = testPlace.location.latitude - 0.15
        performPost("api/places/new", testPlace, PlaceRestDto)

        PlaceRestDto searchCenter = testPlace()
        def nearbyTest = { radius, count ->
            assert  performGet("api/places/nearby",
                    [
                            "lat" : searchCenter.location.latitude,
                            "lng" : searchCenter.location.longitude,
                            "radius": radius
                    ] as Map,
                    PlaceRestDto[]
            ).size() == count
        }

        // now, let's search nearby places with radius to find both
        nearbyTest(2000, 2)
        // now let's shrink radius and find only one
        nearbyTest(1000, 1)
        // finally, let's search too close and find nothing
        nearbyTest(500, 0)
    }

    private static List<PlaceRestDto> getPlaces(Map<String, Object> params = [:]) {
        return rawGet("api/places/", params)['content'] as PlaceRestDto[]
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void placeDeletionRemovesPlaceAndAttachments() throws Exception {

        FileRestDto tempUpload = multipart("api/files/upload-temp", testFile(), FileRestDto)
        PlaceRestDto newPlace = testPlace()
        newPlace.setAttachments([tempUpload])

        PlaceRestDto savedPlace = performPost("api/places/new", newPlace, PlaceRestDto)

        performDelete("api/places/${savedPlace.getId()}")

        assert (getPlaces().size() == 0)
        assert given().get(getUrlWithHost(savedPlace.getAttachments()[0].getFullPath()))
                       .statusCode() == 404
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void attachmentCanBeViewedAfterPlaceCreate() throws Exception {
        MockMultipartFile tempFile = testFile()

        FileRestDto tempUpload = multipart("api/files/upload-temp", tempFile, FileRestDto)

        PlaceRestDto newPlace = testPlace()
        newPlace.setAttachments([tempUpload])

        PlaceRestDto savedPlace = performPost("api/places/new", newPlace, PlaceRestDto)

        byte[] attachContent = getFileContent(savedPlace.getAttachments()[0].getFullPath())

        assert attachContent == tempFile.getBytes()
    }

    private static MockMultipartFile testFile() {
        return new MockMultipartFile("file", "temp_img.png", "image/png", "some file content".getBytes())
    }

    static PlaceRestDto testPlace() {
        PlaceRestDto place = new PlaceRestDto()
        place.setName("Test place")
        place.setPriceFrom(BigDecimal.ONE)
        place.setPriceTo(BigDecimal.TEN)
        place.setDescription("Some test description")
        LocationRestDTO location = new LocationRestDTO()
        location.setLatitude(BigDecimal.ONE)
        location.setLongitude(BigDecimal.TEN)
        location.setAddress("Test street, 5")
        place.setLocation(location)
        return place
    }

}