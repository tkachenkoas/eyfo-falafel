package com.atstudio.eyfofalafel.backend.controller.location;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("location")
public class LocationRestMapper extends SimpleRestObjectMapper<Location, LocationRestDTO> {

    private final GeometryFactory gf;

    public LocationRestMapper(GeometryFactory gf) {
        this.gf = gf;
    }

    @Override
    protected Class<Location> getEntityClass() {
        return Location.class;
    }

    @Override
    protected Class<LocationRestDTO> getRestDtoClass() {
        return LocationRestDTO.class;
    }

    @Override
    public Location toEntity(LocationRestDTO restObject) {
        Location entity = new Location();
        entity.setAddress(restObject.getAddress());
        entity.setCoordinates(
                gf.createPoint(
                        new Coordinate(
                                restObject.getLongitude().doubleValue(),
                                restObject.getLatitude().doubleValue())
                )
        );
        return entity;
    }
}
