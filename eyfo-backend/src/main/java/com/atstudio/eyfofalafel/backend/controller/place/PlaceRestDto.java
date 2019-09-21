package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.files.FileRestDto;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlaceRestDto {
    private Long id;
    private String name;
    private String description;
    private LocationRestDTO location;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private List<FileRestDto> attachments;
    private BigDecimal averageRating;

    public List<FileRestDto> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        return attachments;
    }
}