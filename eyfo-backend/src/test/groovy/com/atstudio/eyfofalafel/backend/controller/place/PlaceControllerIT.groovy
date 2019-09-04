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

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindNearby() {
        def testPlace = testPlace()
        PlaceRestDto savedPlace = performPost("api/places/new", testPlace, PlaceRestDto)

        // make sure that something is found when searching nearby places
        List<PlaceRestDto> places = rawGet("api/places/nearby",
                [
                        "lng" : savedPlace.location.longitude - 0.02,
                        "lat" : savedPlace.location.latitude + 0.02,
                        "radius": 5000
                ] as Map
        )['content'] as PlaceRestDto[]
        assert places.size() == 1
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void lastCreatedOrEditedGoesOnTop() {
        def first = performPost("api/places/new", testPlace("First place"), PlaceRestDto)
        sleep(500)
        def second = performPost("api/places/new", testPlace("Second place"), PlaceRestDto)

        // last created goes first
        assert getPlaces()[0] == second
        sleep(500)
        // last edited goes first
        performPut("api/places/${first.id}", first, PlaceRestDto)

        def places = getPlaces(["searchText" : "place"] as Map)

        assert places[0] == first
        assert places[1] == second
    }

    private static MockMultipartFile testFile() {
        return new MockMultipartFile("file", "temp_img.png", "image/png", "some file content".getBytes())
    }

    static PlaceRestDto testPlace(
            String name = "Test place",
            Integer priceFrom = 1,
            Integer priceTo = 10,
            String description = "Some test description",
            Double latitude = 1,
            Double longitude = 10,
            String address = "Test street, 5"
    ) {
        return [
                "name": name,
                "priceFrom": priceFrom as BigDecimal,
                "priceTo": priceTo as BigDecimal,
                "description": description,
                "location": [
                        "latitude": latitude as BigDecimal,
                        "longitude": longitude as BigDecimal,
                        "address": address
                ] as LocationRestDTO
        ] as PlaceRestDto
    }

}