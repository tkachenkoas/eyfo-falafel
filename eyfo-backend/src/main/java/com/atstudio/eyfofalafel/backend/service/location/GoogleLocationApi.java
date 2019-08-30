package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.google.maps.*;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

@Service
@Slf4j
public class GoogleLocationApi implements LocationApi{

    private GeoApiContext geoApiContext;
    private LocationObjectFactory factory;

    public GoogleLocationApi(GeoApiContext googleApiContext, LocationObjectFactory factory) {
        this.geoApiContext = googleApiContext;
        this.factory = factory;
    }

    @Override
    @SneakyThrows
    public List<String> getAddressSuggestions(String address) {
        PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken(UUID.randomUUID());
        PlaceAutocompleteRequest request = PlacesApi.placeAutocomplete(geoApiContext, address, token).language("ru");
        AutocompletePrediction[] predictions = request.await();
        return Arrays.stream(predictions).map(ac -> ac.description).collect(toList());
    }

    @Override
    @SneakyThrows
    public LocationRestDTO getLocationByAddress(String address) {
        GeocodingResult[] geoCodings = GeocodingApi.geocode(geoApiContext, address).await();
        if (isEmpty(geoCodings)) return null;
        return factory.fromGeocodingResult(geoCodings[0]);
    }

    @Override
    @SneakyThrows
    public String getAddressByLocation(LocationRestDTO location) {
        GeocodingApiRequest request = GeocodingApi.reverseGeocode(geoApiContext, factory.toGoogleLocation(location))
                                                  .language("ru");
        GeocodingResult[] geoCodings = request.await();
        if (isEmpty(geoCodings)) return null;
        return geoCodings[0].formattedAddress;
    }
}
