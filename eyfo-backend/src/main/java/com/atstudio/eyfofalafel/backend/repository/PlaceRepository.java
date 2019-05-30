package com.atstudio.eyfofalafel.backend.repository;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {
}
