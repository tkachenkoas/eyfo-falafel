package com.atstudio.eyfofalafel.backend.service.place

import com.atstudio.eyfofalafel.backend.TestDataSourceAutoConfiguration
import com.atstudio.eyfofalafel.backend.entities.place.Place
import com.atstudio.eyfofalafel.backend.service.files.FileStorageService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD

@RunWith(SpringRunner)
@EnableJpaRepositories(basePackages = ["com.atstudio.eyfofalafel.backend.repository"])
@Import([TestDataSourceAutoConfiguration, PlaceServiceImpl])
@EntityScan("com.atstudio.eyfofalafel.backend.entities")
class PlaceServiceImplTest {

    @MockBean
    private FileStorageService fileStorageService
    @Autowired
    private PlaceServiceImpl placeService

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindByName() {
        List<Place> allPlaces = placeService.findAll(
                ["searchText": "ШаУрМяшНа"] as PlaceFilter,
                PageRequest.of(0, 10)
        ).getContent()
        assert allPlaces.size() == 1
        assert allPlaces[0].getDescription().contains("Информация о шаурм")
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindByDescription() {
        List<Place> allPlaces = placeService.findAll(
                ["searchText": "Описание тестовой"] as PlaceFilter,
                PageRequest.of(0, 10)
        ).getContent()

        assert allPlaces.size() == 1
        assert allPlaces[0].getName().contains("Тестовая фалафельная")
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindByAddress() {
        List<Place> allPlaces = placeService.findAll(
                ["searchText": "адрес"] as PlaceFilter,
                PageRequest.of(0, 10)
        ).getContent()

        assert allPlaces.size() == 1
        assert allPlaces[0].getName().contains("Тестовая фалафельная")
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindByTextPageable() {
        def getPage = { int page ->
            return placeService.findAll(
                    ["searchText": "тестовая"] as PlaceFilter,
                    PageRequest.of(page, 1)
            ).getContent()
        }

        assert getPage(0)[0].getName() != getPage(1)[0].getName()
        assert getPage(2).isEmpty()
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql")
            ,@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql")
            ,@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindNearby() {
        def testFind = { int radius, int count ->
            assert placeService.gerNearbyPlaces(55.62 as BigDecimal, 37.66 as BigDecimal, radius).size() == count
        }
        // find both
        testFind(1800, 2)

        // https://www.geodatasource.com/distance-calculator =>
        // distance between sql test point (37.67274 55.63013) and test point (37.66 55.62) is ~1.38 km =>

        // only closest
        testFind(1400, 1)
        // radius too small
        testFind(1360, 0)
    }
}
