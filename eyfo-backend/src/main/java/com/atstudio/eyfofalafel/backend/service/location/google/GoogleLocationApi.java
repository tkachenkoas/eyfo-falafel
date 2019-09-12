package com.atstudio.eyfofalafel.backend.service.location.google;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.atstudio.eyfofalafel.backend.service.location.LocationApi;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

@Service
public class GoogleLocationApi implements LocationApi {

    private final GoogleApi googleApi;
    private final LocationObjectFactory factory;

    public GoogleLocationApi(GoogleApi googleApi, LocationObjectFactory factory) {
        this.googleApi = googleApi;
        this.factory = factory;
    }

    @Override
    public List<String> getAddressSuggestions(String address) {
        AutocompletePrediction[] predictions = googleApi.getPredictions(address);
        return Stream.of(predictions).map(ac -> ac.description).collect(toList());
    }

    @Override
    public LocationRestDTO getLocationByAddress(String address) {
        GeocodingResult[] geoCodings = googleApi.getGeoCodings(address);
        if (isEmpty(geoCodings)) return null;
        return factory.fromGeocodingResult(geoCodings[0]);
    }

    @Override
    public String getAddressByLocation(LocationRestDTO location) {
        GeocodingResult[] geoCodings = googleApi.reversedGeoCodings(factory.toGoogleLocation(location));
        if (isEmpty(geoCodings)) return null;
        return geoCodings[0].formattedAddress;
    }
}
