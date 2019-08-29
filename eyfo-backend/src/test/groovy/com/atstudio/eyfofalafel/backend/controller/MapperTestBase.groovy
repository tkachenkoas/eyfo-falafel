package com.atstudio.eyfofalafel.backend.controller

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO
import com.atstudio.eyfofalafel.backend.domain.place.Location

class MapperTestBase {

    protected Location testLocation() {
        Location location = new Location()
        location.setAddress("address")
        location.setLatLng(10, 1)
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