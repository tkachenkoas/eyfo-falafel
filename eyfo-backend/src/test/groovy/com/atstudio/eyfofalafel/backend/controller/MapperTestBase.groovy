package com.atstudio.eyfofalafel.backend.controller

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO
import com.atstudio.eyfofalafel.backend.domain.place.Location
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

class MapperTestBase {

    protected Location testLocation() {
        Location location = new Location()
        location.setAddress("address")
        location.setCoordinates(
                new GeometryFactory().createPoint(
                        new Coordinate(1, 10)
                )
        )
        return location
    }

    protected LocationRestDTO testLocationDto() {
        return [
                address: "address",
                latitude: 10,
                longitude: 1
        ] as LocationRestDTO
    }

    protected void assertLocationsEquality(LocationRestDTO restDTO, Location location) {
        assert location.getAddress() == restDTO.getAddress() &&
                location.getLatitude() == restDTO.getLatitude() &&
                location.getLongitude() == restDTO.getLongitude()
    }

}