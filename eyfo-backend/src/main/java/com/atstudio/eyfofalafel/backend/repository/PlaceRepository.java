package com.atstudio.eyfofalafel.backend.repository;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends PagingAndSortingRepository<Place, Long> {

    Page<Place> findAllByOrderByLastEditDesc(Pageable paging);

    @Query(
           "SELECT p from Place p " +
           " WHERE ( lower(p.name) like %:#{#filter.searchText.toLowerCase()}%" +
           " OR lower(p.description) like %:#{#filter.searchText.toLowerCase()}% " +
           " OR lower(p.location.address) like %:#{#filter.searchText.toLowerCase()}%) " +
           " ORDER BY p.lastEdit desc "
    )
    Page<Place> findFiltered(
            @Param("filter") PlaceFilter filter,
            @Param("paging") Pageable paging
    );

    @Query(
            " SELECT p from Place p " +
            " WHERE dwithin(" +
                    " p.location.coordinates, " +
                        "ST_GeogFromText( concat('SRID=4326;POINT (',:lng,' ',:lat,')')), " +
                    ":radius" +
            " ) = true"
    )
    List<Place> findNearbyPlaces(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("radius") Integer radius
    );
}
