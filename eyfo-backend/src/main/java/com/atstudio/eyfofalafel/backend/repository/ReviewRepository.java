package com.atstudio.eyfofalafel.backend.repository;

import com.atstudio.eyfofalafel.backend.entities.review.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    List<Review> findAllByPlaceId(Long placeId);
}
