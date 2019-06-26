package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.repository.PlaceRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository crudRepo;

    public PlaceServiceImpl(PlaceRepository crudRepo) {
        this.crudRepo = crudRepo;
    }

    @Override
    public List<Place> findAll() {
        return Lists.newArrayList(crudRepo.findAll());
    }

    @Override
    public Place save(Place place) {
        return crudRepo.save(place);
    }

    @Override
    public Optional<Place> findById(Long id)  {
        return crudRepo.findById(id);
    }
}
