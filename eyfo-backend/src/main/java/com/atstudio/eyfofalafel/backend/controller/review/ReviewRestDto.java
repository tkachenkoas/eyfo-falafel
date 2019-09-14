package com.atstudio.eyfofalafel.backend.controller.review;

import com.atstudio.eyfofalafel.backend.controller.auth.UserRestDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRestDto {
    private Long id;
    private Long placeId;
    private UserRestDto author;
    private Integer rating;
    private String comment;
    private LocalDateTime creationDateTime;
}
