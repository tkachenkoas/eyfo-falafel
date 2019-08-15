package com.atstudio.eyfofalafel.backend.repository

import com.atstudio.eyfofalafel.backend.TestDataSourceAutoConfiguration
import com.atstudio.eyfofalafel.backend.domain.place.Place
import com.atstudio.eyfofalafel.backend.service.place.PlaceFilter
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
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
@Import([TestDataSourceAutoConfiguration])
@EntityScan("com.atstudio.eyfofalafel.backend.domain")
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindByName() {
        List<Place> allPlaces = placeRepository.findFiltered(
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
        List<Place> allPlaces = placeRepository.findFiltered(
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
    void testFindByTextPageable() {
        def getPage = { int page ->
            return placeRepository.findFiltered(
                    ["searchText": "тестовая"] as PlaceFilter,
                    PageRequest.of(page, 1)
            ).getContent()
        }

        assert getPage(0)[0].getName() != getPage(1)[0].getName()
        assert getPage(2).isEmpty()
    }


}
