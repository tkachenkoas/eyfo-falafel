package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoogleLocationApi implements LocationApi{

    private static Logger log;
    private GeoApiContext context;
    private LocationObjectFactory factory;

    public GoogleLocationApi(GeoApiContext context, Logger logger, LocationObjectFactory factory) {
        this.context = context;
        this.log = logger;
        this.factory = factory;
    }

    @Override
    public List<String> getAddressSuggestions(String address) throws Exception {
        AutocompletePrediction[] predictions = PlacesApi.placeAutocomplete(context, address, new PlaceAutocompleteRequest.SessionToken(UUID.randomUUID())).await();
        return Arrays.stream(predictions).map(ac -> ac.description).collect(Collectors.toList());
    }

    @Override
    public Location getLocationByAddress(String address) throws Exception {
        GeocodingResult[] geoCodings = GeocodingApi.geocode(context, address).await();
        if (ArrayUtils.isEmpty(geoCodings)) return null;
        return factory.fromGeocodingResult(geoCodings[0]);
    }

    @Override
    public String getAddressByLocation(Location location) throws Exception {
        GeocodingResult[] geoCodings = GeocodingApi.reverseGeocode(context, factory.toGoogleLocation(location)).await();
        if (ArrayUtils.isEmpty(geoCodings)) return null;
        return geoCodings[0].formattedAddress;
    }
}
