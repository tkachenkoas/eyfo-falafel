package com.atstudio.eyfofalafel.backend.service.location.google;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.SneakyThrows;

import java.util.UUID;

import static com.google.maps.GeocodingApi.geocode;
import static com.google.maps.GeocodingApi.reverseGeocode;
import static com.google.maps.PlacesApi.placeAutocomplete;

public class StaticWrapperGoogleApiImpl implements GoogleApi {

    private final GeoApiContext geoApiContext;

    public StaticWrapperGoogleApiImpl(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    @SneakyThrows
    public AutocompletePrediction[] getPredictions(String address) {
        return placeAutocomplete(
                geoApiContext,
                address,
                new PlaceAutocompleteRequest.SessionToken(UUID.randomUUID())
        ).language("ru").await();
    }

    @Override
    @SneakyThrows
    public GeocodingResult[] getGeoCodings(String address) {
        return geocode(geoApiContext, address).await();
    }

    @Override
    @SneakyThrows
    public GeocodingResult[] reversedGeoCodings(LatLng latLng) {
        return reverseGeocode(geoApiContext, latLng).language("ru").await();
    }
}
