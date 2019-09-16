package com.atstudio.eyfofalafel.backend.controller.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LocationRestDTO {

    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static LocationRestDTO ofLatLng(Double lat, Double lng) {
        LocationRestDTO resut = new LocationRestDTO();
        resut.setLongitude(valueOf(lng));
        resut.setLatitude(valueOf(lat));
        return resut;
    }
}
