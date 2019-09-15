package com.atstudio.eyfofalafel.backend.controller.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRestDto {
    private Long id;
    private Long placeId;
    private Integer rating;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime creationDateTime;
}
