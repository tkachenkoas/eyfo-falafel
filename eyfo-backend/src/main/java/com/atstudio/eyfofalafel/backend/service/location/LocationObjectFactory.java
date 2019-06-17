package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Component;

@Component
public class LocationObjectFactory {

    public LatLng toGoogleLocation(Location location) {
        LatLng latLng = new LatLng();
        latLng.lat = location.getLatitude().doubleValue();
        latLng.lng = location.getLongitude().doubleValue();
        return latLng;
    }

    public Location fromGeocodingResult(GeocodingResult geoCoding) {
        LatLng latLng = geoCoding.geometry.location;
        Location location = Location.ofLatLng(latLng.lat, latLng.lng);
        location.setAddress(geoCoding.formattedAddress);
        return location;
    }

}
