package com.example.apidb;

import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.*;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.example.apidb.TestHelper.mapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationIT {

    @Value("${json.locations}")
    private String locationsJson;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    LocationRepository locationRepository;

    @MockBean
    CompanyRepository companyRepository;
    @MockBean
    ShipmentRepository shipmentRepository;
    @MockBean
    ContactRepository contactRepository;

    @LocalServerPort
    private int port;
    HttpHeaders headers = new HttpHeaders();
    List<Location> locationList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        TypeReference<List<Location>> locationsRef = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(locationsJson);
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            locationList = mapper.readValue(inputStream, locationsRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetLocation() {

        ResponseEntity<Location> results = restTemplate.exchange("http://localhost:" + port + "/api/location?id=3",
                HttpMethod.GET,
                null,
                Location.class);

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(locationList.get(2)).usingRecursiveComparison().isEqualTo(results.getBody());
    }

    @Test
    public void testGetLocations() {

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertTrue(!results.getBody().isEmpty());
    }

    @Test
    public void testSaveLocation() {

        Location location = Location.builder()
                .city("Fayetteville")
                .street("street")
                .zipcode("zipcode")
                .suite("suite")
                .lat(32.33)
                .lon(37.49)
                .name("Big place")
                .timeZone("CST")
                .locationType(LocationType.BUSINESS)
                .State("Arkansas").build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/save", location, String.class);

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Location> resultBody = results.getBody();
        location.setId(Long.valueOf(resultBody.size()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(location);
    }
    @Test
    public void testSaveLocation_nullValues() {

        Location location = Location.builder().build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/save", location, String.class);

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Location> resultBody = results.getBody();
        location.setId(Long.valueOf(resultBody.size()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(location);
    }

    @Test
    public void testSaveLocation_exception() {

        String file = "src/test/java/com/example/apidb/resources/json/location_exception.json";
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> request;

        request = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/save", request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Assertions.assertTrue(responseEntity.getBody().contains("Unable to save location error:"));
    }

    @Test
    public void testSaveAllLocations() {

        List<Location> locations = new ArrayList<>();
        locations.add(Location.builder().name("wiggle").build());
        locations.add(Location.builder().name("wobble").build());
        locations.add(Location.builder().name("knobble").build());

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/saveAll", locations, String.class);

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Location> resultBody = results.getBody();
        int bodySize = resultBody.size();
        locations.get(0).setId((long) (bodySize - 2));
        locations.get(1).setId((long) (bodySize - 1));
        locations.get(2).setId((long) (bodySize));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.subList(bodySize - 3, bodySize)).usingRecursiveComparison().isEqualTo(locations);
    }
}
