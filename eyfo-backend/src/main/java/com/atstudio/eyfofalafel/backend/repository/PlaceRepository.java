package com.atstudio.eyfofalafel.backend.repository;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceFilter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {

    @Query(
           "SELECT p from Place p " +
           " WHERE lower(p.name) like %:#{#filter.searchText.toLowerCase()}%" +
           " OR lower(p.description) like %:#{#filter.searchText.toLowerCase()}%"
    )
    Iterable<Place> findFiltered(@Param("filter") PlaceFilter filter);

}
