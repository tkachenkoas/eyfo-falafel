package com.atstudio.eyfofalafel.backend.service.location.google;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Component;

@Component
public class LocationObjectFactory {

    LatLng toGoogleLocation(LocationRestDTO location) {
        LatLng latLng = new LatLng();
        latLng.lat = location.getLatitude().doubleValue();
        latLng.lng = location.getLongitude().doubleValue();
        return latLng;
    }

    LocationRestDTO fromGeocodingResult(GeocodingResult geoCoding) {
        LatLng latLng = geoCoding.geometry.location;
        LocationRestDTO result = LocationRestDTO.ofLatLng(latLng.lat, latLng.lng);
        result.setAddress(geoCoding.formattedAddress);
        return result;
    }

}
