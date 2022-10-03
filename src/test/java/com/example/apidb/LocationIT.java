package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.*;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.apidb.TestHelper.mapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        try {
            locationList = mapper.readValue(inputStream, locationsRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetLocation() {

        ResponseEntity<Location> results = restTemplate.exchange("http://localhost:" + port + "/api/location?id=99",
                HttpMethod.GET,
                null,
                Location.class);

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(locationList.get(2), results.getBody());
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
        assertEquals(3, results.getBody().size());
    }

    @Test
    public void testSaveLocation() {

        Location location = Location.builder()
                .id(Long.valueOf(1))
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

        locationList.add(0, location);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(4, results.getBody().size());
        assertEquals(locationList, results.getBody());
    }
    @Test
    public void testSaveLocation_nullValues() {

        Location location = Location.builder().id(Long.valueOf(2)).build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/save", location, String.class);

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        locationList.add(0, location);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(5, results.getBody().size());
        assertEquals(locationList, results.getBody());
    }
}
