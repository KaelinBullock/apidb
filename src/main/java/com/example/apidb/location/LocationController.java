package com.example.apidb.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/location")
@Slf4j
public class LocationController {

    LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public Optional<Location> getLocation(@RequestParam Long id) {
        return locationService.getLocation(id);
    }

    @GetMapping("/list")
    public Iterable<Location> getLocations() {
        return locationService.getLocations();
    }


    @PostMapping("/save")
    public void save(@RequestBody Location location) {
        locationService.save(location);
    }

}
