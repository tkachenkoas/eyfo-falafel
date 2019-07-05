package com.atstudio.eyfofalafel.backend.domain.place;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "t_places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Embedded
    private Location location;

    @Column(name = "price_from")
    private BigDecimal priceFrom;

    @Column(name = "price_to")
    private BigDecimal priceTo;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "t_places_attachments",
            joinColumns = { @JoinColumn(name = "attachment_id") },
            inverseJoinColumns = { @JoinColumn(name = "place_id") }
    )
    private List<Attachment> attachments;

}
