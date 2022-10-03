package com.example.apidb;


import com.example.apidb.location.Location;
import com.example.apidb.location.LocationController;
import com.example.apidb.location.LocationRepository;
import com.example.apidb.location.LocationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationRepositoryTests {

    private LocationController locationController;

    @Autowired
    LocationRepository locationRepository;

    @Before
    public void setup() {
        LocationService locationService = new LocationService(locationRepository);
        this.locationController = new LocationController(locationService);
    }

    @Test
    @DisplayName("Test get locations")
    public void getLocationList() {
        String s = String.valueOf(locationRepository.findAll());

        Iterable<Location> actual = locationController.getLocations();
        List<Location> expected = Collections.emptyList();
        assertThat(actual, equalTo(expected));
    }
}