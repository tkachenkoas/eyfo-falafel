package com.atstudio.eyfofalafel.backend.controller.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRestDto {
    private Long id;
    private Long placeId;
    private Integer rating;
    private String comment;
    private LocalDateTime creationDateTime;
}
