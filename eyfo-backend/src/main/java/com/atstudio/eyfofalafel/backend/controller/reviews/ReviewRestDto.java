package com.atstudio.eyfofalafel.backend.controller.reviews;

import com.atstudio.eyfofalafel.backend.controller.auth.UserRestDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRestDto {
    private Long placeId;
    private UserRestDto author;
    private Integer rating;
    private LocalDateTime creationDateTime;
}
