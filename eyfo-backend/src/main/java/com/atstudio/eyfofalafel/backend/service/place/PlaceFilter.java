package com.atstudio.eyfofalafel.backend.service.place;

import lombok.AllArgsConstructor;
import lombok.Data;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Data
@AllArgsConstructor
public class PlaceFilter {
    private String searchText;
    public boolean isEmpty() {
        return isBlank(searchText);
    }
}
