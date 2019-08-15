package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface PlaceService {

    Collection<Place> findAll(Optional<PlaceFilter> filterOptional, Pageable paging);
    Place save(Place place);
    Place findByIdOrThrow(Long id);
    void deleteById(Long id);
}
