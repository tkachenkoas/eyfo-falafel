package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationApi {

    List<String> getAddressSuggestions(String address) throws Exception;
    Location getLocationByAddress(String address) throws Exception;
    String getAddressByLocation(Location location) throws Exception;

}
