package com.atstudio.eyfofalafel.backend.controller.location;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.entities.place.Location;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationRestMapper extends SimpleRestObjectMapper<Location, LocationRestDTO> {

    private final GeometryFactory gf;

    @Autowired
    public LocationRestMapper(GeometryFactory gf) {
        super();
        this.gf = gf;
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
