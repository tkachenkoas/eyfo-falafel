package com.atstudio.eyfofalafel.backend.controller.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LocationRestDTO {

    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationRestDTO restDTO = (LocationRestDTO) o;
        return new EqualsBuilder()
                .append(address, restDTO.address)
                .append(latitude.stripTrailingZeros(), restDTO.latitude.stripTrailingZeros())
                .append(longitude.stripTrailingZeros(), restDTO.longitude.stripTrailingZeros())
                .isEquals();
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
