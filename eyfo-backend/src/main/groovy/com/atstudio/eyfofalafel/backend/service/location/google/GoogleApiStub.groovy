package com.atstudio.eyfofalafel.backend.service.location.google

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.maps.model.AutocompletePrediction
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng

class GoogleApiStub implements GoogleApi {

    private final AutocompletePrediction[] predictions
    private final GeocodingResult[] addressGeocodings
    private final GeocodingResult[] reversedGeoCodings

    private final ObjectMapper mapper

    GoogleApiStub(
            File autoCompleteStub,
            File geocodeStub,
            File reverseGeocodeStub,
            ObjectMapper mapper
    ) {
        this.mapper = mapper
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

        predictions = parse(autoCompleteStub, 'predictions', AutocompletePrediction[])
        addressGeocodings = parse(geocodeStub, 'results', GeocodingResult[])
        reversedGeoCodings = parse(reverseGeocodeStub, 'results', GeocodingResult[])
    }

    private <T> T parse(File file, String field, Class<T> targetClass) {
        JsonNode node = mapper.readValue(file.text, ObjectNode).get(field)
        return mapper.treeToValue(node, targetClass)
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
