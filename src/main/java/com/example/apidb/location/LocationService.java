package com.example.apidb.location;

import com.example.apidb.location.Location;
import com.example.apidb.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Iterable<Location> getLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocation(Long id) {
        return locationRepository.findById(id);
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public void save(List<Location> locations) {
        locationRepository.saveAll(locations);
    }
}
