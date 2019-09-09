package com.atstudio.eyfofalafel.backend.service.location.google

import com.google.maps.model.AutocompletePrediction
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import groovy.json.JsonSlurper

class GoogleApiStub implements GoogleApi {

    private final AutocompletePrediction[] predictions
    private final GeocodingResult[] addressGeocodings
    private final GeocodingResult[] reversedGeoCodings

    GoogleApiStub(
            File autoCompleteStub,
            File geocodeStub,
            File reverseGeocodeStub
    ) {
        predictions = parse(autoCompleteStub)['predictions'] as AutocompletePrediction[]
        addressGeocodings = parse(geocodeStub)['results'] as GeocodingResult[]
        reversedGeoCodings = parse(reverseGeocodeStub)['results'] as GeocodingResult[]
    }

    private Object parse(File file) {
        return new JsonSlurper().parseText(file.text)
    }

    @Override
    AutocompletePrediction[] getPredictions(String address) {
        return predictions
    }

    @Override
    GeocodingResult[] getGeoCodings(String address) {
        return addressGeocodings
    }

    @Override
    GeocodingResult[] reversedGeoCodings(LatLng latLng) {
        return reversedGeoCodings
    }
}
