package com.atstudio.eyfofalafel.backend.entities.review;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_reviews_id")
    @SequenceGenerator(name="t_reviews_id", sequenceName="hibernate_sequence", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "creation_date")
    private LocalDateTime creationDateTime;

}
