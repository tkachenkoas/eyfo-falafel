package com.atstudio.eyfofalafel.backend.service.location.google;

import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public interface GoogleApi {
    AutocompletePrediction[] getPredictions(String address);
    GeocodingResult[] getGeoCodings(String address);
    GeocodingResult[] reversedGeoCodings(LatLng latLng);
}
