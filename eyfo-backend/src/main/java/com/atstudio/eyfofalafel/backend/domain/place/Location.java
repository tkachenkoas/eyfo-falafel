package com.atstudio.eyfofalafel.backend.domain.place;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Data
public class Location {

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
