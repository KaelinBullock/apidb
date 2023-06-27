package com.example.apidb.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/location")
@Slf4j
@CrossOrigin
public class LocationController {

    final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public Optional<Location> getLocation(@RequestParam Long id) {
        return locationService.getLocation(id);
    }

    @GetMapping("/getLocationsByName")
    public Iterable<Location> getCompaniesByName(@RequestParam String name) {
        return locationService.getLocationByName(name);
    }

    @GetMapping("/list")
    public Iterable<Location> getLocations() {
        return locationService.getLocations();
    }


    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Location location) {
        HttpHeaders headers = new HttpHeaders();
        try {
            locationService.save(location);
            return new ResponseEntity<>(
                    "Location has been saved", headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(
                    "Unable to save location error: " + e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveAll")
    public void save(@RequestBody List<Location> locations) {
        locationService.save(locations);
    }
}
