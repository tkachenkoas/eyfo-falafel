package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.google.maps.*;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
    public List<String> getAddressSuggestions(String address) throws Exception {
        PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken(UUID.randomUUID());
        PlaceAutocompleteRequest request = PlacesApi.placeAutocomplete(geoApiContext, address, token).language("ru");
        AutocompletePrediction[] predictions = request.await();
        return Arrays.stream(predictions).map(ac -> ac.description).collect(toList());
    }

    @Override
    public Location getLocationByAddress(String address) throws Exception {
        GeocodingResult[] geoCodings = GeocodingApi.geocode(geoApiContext, address).await();
        if (ArrayUtils.isEmpty(geoCodings)) return null;
        return factory.fromGeocodingResult(geoCodings[0]);
    }

    @Override
    public String getAddressByLocation(Location location) throws Exception {
        GeocodingApiRequest request = GeocodingApi.reverseGeocode(geoApiContext, factory.toGoogleLocation(location))
                                                  .language("ru");
        GeocodingResult[] geoCodings = request.await();
        if (ArrayUtils.isEmpty(geoCodings)) return null;
        return geoCodings[0].formattedAddress;
    }
}
