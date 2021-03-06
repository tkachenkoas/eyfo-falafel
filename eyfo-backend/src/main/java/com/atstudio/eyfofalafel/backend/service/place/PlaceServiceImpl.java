package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.entities.files.Attachment;
import com.atstudio.eyfofalafel.backend.entities.place.Place;
import com.atstudio.eyfofalafel.backend.repository.PlaceRepository;
import com.atstudio.eyfofalafel.backend.service.files.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Service
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepo;
    private FileStorageService fileStorageService;

    public PlaceServiceImpl(PlaceRepository placeRepo, FileStorageService fileStorageService) {
        this.placeRepo = placeRepo;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Page<Place> findAll(PlaceFilter filter, Pageable paging) {
        return filter.isEmpty()
                ? placeRepo.findAllByOrderByLastEditDesc(paging)
                : placeRepo.findFiltered(filter, paging);
    }

    @Override
    @Transactional
    public Place save(Place placeToSave) {
        List<Attachment> newAttachments = storeAll(placeToSave.getAttachments());

        if (placeToSave.getId() == null) {
            placeToSave.setAttachments(newAttachments);
            return placeRepo.save(placeToSave);
        }

        Place existingPlace = findByIdOrThrow(placeToSave.getId());
        for (Attachment existing: existingPlace.getAttachments()) {
            if (!newAttachments.contains(existing)){
                fileStorageService.remove(existing);
            }
        }
        existingPlace.setName(placeToSave.getName());
        existingPlace.setLocation(placeToSave.getLocation());
        existingPlace.setAttachments(newAttachments);

        return placeRepo.save(existingPlace);
    }

    private List<Attachment> storeAll(List<Attachment> attachments) {
        return emptyIfNull(attachments).stream()
                          .map(att -> fileStorageService.saveFileForStorage(att))
                          .collect(toList());
    }

    @Override
    public Place findByIdOrThrow(Long id)  {
        return placeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't find place with id=" + id));
    }

    @Override
    public void deleteById(Long id) {
        Place existingPlace = findByIdOrThrow(id);
        existingPlace.getAttachments().forEach(fileStorageService::remove);
        this.placeRepo.deleteById(id);
    }

    @Override
    public List<Place> gerNearbyPlaces(BigDecimal lat, BigDecimal lng, Integer radius) {
        return placeRepo.findNearbyPlaces(lat.doubleValue(), lng.doubleValue(), radius);
    }
}
