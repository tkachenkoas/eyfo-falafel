package com.atstudio.eyfofalafel.backend.service.place

import com.atstudio.eyfofalafel.backend.domain.place.Place
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import static java.util.Optional.ofNullable
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = PlaceServiceTestConfig)
class PlaceServiceImplTest {

    @Autowired
    private PlaceService placeServiceImpl

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindWithoutFilter() {
        Collection<Place> allPlaces = placeServiceImpl.findAll(Optional.empty())

        assert allPlaces.size() == 2
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void testFindByName() {
        Collection<Place> allPlaces = placeServiceImpl.findAll(
                ofNullable(["searchText" : "ШаУрМяшНа"] as PlaceFilter)
        )

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
        Collection<Place> allPlaces = placeServiceImpl.findAll(
                ofNullable(["searchText" : "Описание тестовой"] as PlaceFilter)
        )

        assert allPlaces.size() == 1
        assert allPlaces[0].getName().contains("Тестовая фалафельная")
    }

}
