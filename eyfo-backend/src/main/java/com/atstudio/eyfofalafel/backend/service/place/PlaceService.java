package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.entities.place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface PlaceService {

    Page<Place> findAll(PlaceFilter filter, Pageable paging);
    Place save(Place place);
    Place findByIdOrThrow(Long id);
    void deleteById(Long id);
    List<Place> gerNearbyPlaces(BigDecimal lat, BigDecimal lng, Integer radius);
}
