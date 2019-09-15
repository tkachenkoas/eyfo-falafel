package com.atstudio.eyfofalafel.backend.controller.review;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.entities.review.Review;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Component
public class ReviewRestMapper extends SimpleRestObjectMapper<Review, ReviewRestDto> {

    @Override
    public Review toEntity(ReviewRestDto restObject) {
        Review result = super.toEntity(restObject);
        result.setId(null);
        result.setCreationDateTime(now());
        return result;
    }
}
