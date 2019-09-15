package com.atstudio.eyfofalafel.backend.controller.review;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.entities.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewRestMapper extends SimpleRestObjectMapper<Review, ReviewRestDto> {

    @Override
    protected Class<Review> getEntityClass() {
        return Review.class;
    }

    @Override
    protected Class<ReviewRestDto> getRestDtoClass() {
        return ReviewRestDto.class;
    }
}
