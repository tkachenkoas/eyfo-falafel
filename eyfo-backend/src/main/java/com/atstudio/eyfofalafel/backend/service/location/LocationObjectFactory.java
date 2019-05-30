package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class LocationObjectFactory {

    public LatLng toGoogleLocation(Location location) {
        LatLng latLng = new LatLng();
        latLng.lat = location.getLatitude();
        latLng.lng = location.getLongitude();
        return latLng;
    }

    public Location fromGeocodingResult(GeocodingResult geoCoding) {
        Location location = new Location();
        location.setLatitude(geoCoding.geometry.location.lat);
        location.setLongitude(geoCoding.geometry.location.lng);
        location.setAddress(geoCoding.formattedAddress);
        return location;
    }

}
