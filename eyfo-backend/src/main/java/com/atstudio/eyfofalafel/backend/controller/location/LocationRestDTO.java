package com.atstudio.eyfofalafel.backend.controller.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LocationRestDTO {

    private Long id;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
