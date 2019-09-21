package com.atstudio.eyfofalafel.backend.service.location.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.Charset;

public class GoogleApiStub implements GoogleApi {

    private final AutocompletePrediction[] predictions;
    private final GeocodingResult[] addressGeocodings;
    private final GeocodingResult[] reversedGeoCodings;
    private final ObjectMapper mapper;

    public GoogleApiStub(File autoCompleteStub, File geocodeStub, File reverseGeocodeStub, ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        predictions = parse(autoCompleteStub, "predictions", AutocompletePrediction[].class);
        addressGeocodings = parse(geocodeStub, "results", GeocodingResult[].class);
        reversedGeoCodings = parse(reverseGeocodeStub, "results", GeocodingResult[].class);
    }

    @SneakyThrows
    private <T> T parse(File file, String field, Class<T> targetClass) {
        JsonNode node = mapper.readValue(
                Files.toString(file, Charset.defaultCharset()),
                ObjectNode.class
        ).get(field);
        return mapper.treeToValue(node, targetClass);
    }

    @Override
    public AutocompletePrediction[] getPredictions(String address) {
        return predictions;
    }

    @Override
    public GeocodingResult[] getGeoCodings(String address) {
        return addressGeocodings;
    }

    @Override
    public GeocodingResult[] reversedGeoCodings(LatLng latLng) {
        return reversedGeoCodings;
    }

}
