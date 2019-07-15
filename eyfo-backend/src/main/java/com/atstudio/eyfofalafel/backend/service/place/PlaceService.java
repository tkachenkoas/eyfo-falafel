package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaceService {

    List<Place> findAll();
    Place save(Place place);
    Place findByIdOrThrow(Long id);

}
