package com.atstudio.eyfofalafel.backend.service.place;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceFilter {
    private String searchText;
    private BigDecimal longitude;
    private BigDecimal latitude;
}
