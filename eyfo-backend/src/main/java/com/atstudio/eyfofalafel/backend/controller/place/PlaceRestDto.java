package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Optional.ofNullable;

@Data
@JsonInclude(content = JsonInclude.Include.NON_NULL)
public class PlaceRestDto {
    private Long id;
    private String name;
    private String description;
    private LocationRestDTO location;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private List<FileRestDto> attachments;
    private BigDecimal averageRating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceRestDto restDto = (PlaceRestDto) o;
        return new EqualsBuilder()
                .append(name, restDto.name)
                .append(description, restDto.description)
                .append(location, restDto.location)
                .append(strip(priceFrom), strip(restDto.priceFrom))
                .append(strip(priceTo), strip(restDto.priceTo))
                .append(strip(averageRating), strip(restDto.averageRating))
                .isEquals();
    }

    private BigDecimal strip(BigDecimal source) {
        return ofNullable(source)
                .map(BigDecimal::stripTrailingZeros)
                .orElse(null);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(description)
                .append(location)
                .append(priceFrom)
                .append(priceTo)
                .append(priceTo)
                .append(averageRating)
                .toHashCode();
    }
}