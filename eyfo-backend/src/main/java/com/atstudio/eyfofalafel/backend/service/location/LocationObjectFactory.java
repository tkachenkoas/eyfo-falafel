package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Component;

@Component
public class LocationObjectFactory {

    public LatLng toGoogleLocation(LocationRestDTO location) {
        LatLng latLng = new LatLng();
        latLng.lat = location.getLatitude().doubleValue();
        latLng.lng = location.getLongitude().doubleValue();
        return latLng;
    }

    public LocationRestDTO fromGeocodingResult(GeocodingResult geoCoding) {
        LatLng latLng = geoCoding.geometry.location;
        LocationRestDTO result = LocationRestDTO.ofLatLng(latLng.lat, latLng.lng);
        result.setAddress(geoCoding.formattedAddress);
        return result;
    }

}
