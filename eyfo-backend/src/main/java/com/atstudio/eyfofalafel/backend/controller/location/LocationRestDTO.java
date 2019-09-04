package com.atstudio.eyfofalafel.backend.controller.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.util.Optional.ofNullable;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationRestDTO restDTO = (LocationRestDTO) o;
        return new EqualsBuilder()
                .append(address, restDTO.address)
                .append(strip(latitude), strip(restDTO.latitude))
                .append(strip(longitude), strip(restDTO.longitude))
                .isEquals();
    }

    private BigDecimal strip(BigDecimal value) {
        return ofNullable(value)
                .map(BigDecimal::stripTrailingZeros)
                .orElse(null);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(address)
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }
}
