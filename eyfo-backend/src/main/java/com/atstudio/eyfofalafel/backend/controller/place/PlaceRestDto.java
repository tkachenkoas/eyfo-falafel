package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(content = JsonInclude.Include.NON_NULL)
class PlaceRestDto {
    private Long id;
    private String name;
    private String description;
    private LocationRestDTO location;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private List<FileRestDto> attachments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceRestDto restDto = (PlaceRestDto) o;
        return new EqualsBuilder()
                .append(name, restDto.name)
                .append(description, restDto.description)
                .append(location, restDto.location)
                .append(priceFrom.stripTrailingZeros(), restDto.priceFrom.stripTrailingZeros())
                .append(priceTo.stripTrailingZeros(), restDto.priceTo.stripTrailingZeros())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(description)
                .append(location)
                .append(priceFrom)
                .append(priceTo)
                .toHashCode();
    }
}