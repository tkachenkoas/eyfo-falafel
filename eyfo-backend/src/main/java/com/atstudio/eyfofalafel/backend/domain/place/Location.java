package com.atstudio.eyfofalafel.backend.domain.place;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "t_locations")
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    public static Location ofLatLng(Double lat, Double lng) {
        Location location = new Location();
        location.latitude = new BigDecimal(lat);
        location.longitude = new BigDecimal(lng);
        return location;
    }

}
