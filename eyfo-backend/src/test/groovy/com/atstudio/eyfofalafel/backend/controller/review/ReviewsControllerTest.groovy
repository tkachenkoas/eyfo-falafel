package com.atstudio.eyfofalafel.backend.controller.review

import com.atstudio.eyfofalafel.backend.TestDataSourceAutoConfiguration
import com.atstudio.eyfofalafel.backend.controller.place.PlaceRestDto
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner

import java.time.LocalDateTime

import static com.atstudio.eyfofalafel.backend.controller.place.PlaceControllerIT.places
import static com.atstudio.eyfofalafel.backend.testutil.TestRequestUtils.rawPost
import static com.atstudio.eyfofalafel.backend.testutil.TestRequestUtils.typedGet
import static java.time.temporal.ChronoUnit.SECONDS
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD

@RunWith(SpringRunner)
@Import(TestDataSourceAutoConfiguration)
class ReviewsControllerTest {

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void newReviewCanBeAddedAndRetrieved() {
        PlaceRestDto place = (getPlaces() as List)[0]

        def review = testReview()
        def createdReview = rawPost("api/places/${place.getId()}/reviews/new", review)

        def loaded = typedGet("api/places/${place.getId()}/reviews/", [:], ArrayList) as List

        assert loaded.size() == 1

        loaded << createdReview
        loaded.each { rv ->
            assert rv['placeId'] == place.getId() &&
                    rv['rating'] == review['rating'] &&
                    rv['comment'] == review['comment']
            assert SECONDS.between(LocalDateTime.parse(rv['creationDateTime']), LocalDateTime.now()) < 1
        }
    }

    @Test
    @SqlGroup([
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/clean_db.sql"),
            @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:/places/test_place_data.sql"),
            @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:/clean_db.sql")
    ])
    void placeRatingIsAlteredAfterAddingReviews() {
        PlaceRestDto existingPlace = getPlaces()[0]

        assert existingPlace['averageRating'] == null

        rawPost("api/places/${existingPlace.getId()}/reviews/new", testReview(5))
        assert getPlaces()[0]['averageRating'] == 5

        rawPost("api/places/${existingPlace.getId()}/reviews/new", testReview(4))
        assert getPlaces()[0]['averageRating'] == 4.5
    }

    static Object testReview(Integer rating = 5, String text = 'Some review text') {
        return [
                "rating": rating,
                "comment": text
        ]
    }

}
