package com.atstudio.eyfofalafel.backend.service.review;

import com.atstudio.eyfofalafel.backend.entities.review.Review;
import com.atstudio.eyfofalafel.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findAllByPlace(Long placeId) {
        return reviewRepository.findAllByPlaceId(placeId);
    }
}
