package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {

    List<String> getAddressSuggestions(String searchStr);
    Location getLocationByAddress(String address);
    String getAddressByLocation(Location location);

}


