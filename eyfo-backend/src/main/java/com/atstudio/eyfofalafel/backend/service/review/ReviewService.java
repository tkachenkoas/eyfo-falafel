package com.atstudio.eyfofalafel.backend.service.review;

import com.atstudio.eyfofalafel.backend.entities.review.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(Review review);
    List<Review> findAllByPlace(Long placeId);
}
