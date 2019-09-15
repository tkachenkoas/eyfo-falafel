package com.atstudio.eyfofalafel.backend.controller.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/places/{placeId}/reviews")
@Slf4j
public class ReviewsController {

    @PostMapping("/new")
    public ResponseEntity<ReviewRestDto> addReview(@PathVariable Long placeId, ReviewRestDto review) {

        return null;
    }

}
