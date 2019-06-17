package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(content = JsonInclude.Include.NON_NULL)
class PlaceRestDto {
    private Long id;
    private String name;
    private String description;
    private LocationRestDTO location;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
}