package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PlaceService {

    List<Place> findAll();
    Place save(Place place);
    Optional<Place> findById(Long id);

}
