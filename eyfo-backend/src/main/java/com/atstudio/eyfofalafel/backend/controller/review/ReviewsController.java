package com.atstudio.eyfofalafel.backend.controller.review;

import com.atstudio.eyfofalafel.backend.service.review.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/places/{placeId}/reviews")
@Slf4j
public class ReviewsController {

    private final ReviewRestMapper restMapper;
    private final ReviewService reviewService;

    @Autowired
    public ReviewsController(ReviewRestMapper restMapper, ReviewService reviewService) {
        this.restMapper = restMapper;
        this.reviewService = reviewService;
    }

    @PostMapping("/new")
    public ResponseEntity<ReviewRestDto> addReview(@PathVariable Long placeId, @RequestBody ReviewRestDto review) {
        log.info("Received add review request {} for placeId = {}", review, placeId);
        review.setPlaceId(placeId);
        return ResponseEntity.ok(
                restMapper.toRest(
                        reviewService.createReview(restMapper.toEntity(review))
                )
        );
    }

    @GetMapping("/")
    public ResponseEntity<List<ReviewRestDto>> getReviews(@PathVariable Long placeId) {
        return ResponseEntity.ok(
                reviewService.findAllByPlace(placeId).stream()
                .map(restMapper::toRest).collect(toList())
        );
    }

}
