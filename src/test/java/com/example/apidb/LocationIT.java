package com.example.apidb;

import com.example.apidb.company.CompanyController;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.company.CompanyService;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.contact.ContactService;
import com.example.apidb.location.*;
import com.example.apidb.shipment.ShipmentRepository;
import com.example.apidb.shipment.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApiDbApplication.class, H2JpaConfig.class})
//@Transactional
//@EnableJpaRepositories(basePackageClasses = { LocationRepository.class})//https://stackoverflow.com/questions/45663025/spring-data-jpa-multiple-enablejparepositories
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:/application.properties")
//@ActiveProfiles("test")
public class LocationIT {

    @MockBean
    CompanyRepository companyRepository;
    @MockBean
    ShipmentRepository shipmentRepository;
    @MockBean
    ContactRepository contactRepository;
    @Autowired
    LocationRepository locationRepository;

    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate;
    HttpHeaders headers = new HttpHeaders();
    List<Location> locationList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>(){});
        locationList.addAll(results.getBody());
    }

    @Test
    public void testGetLocation() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        Location location = Location.builder()
                .id(Long.valueOf(7))
                .city("Fayetteville")
                .street("street")
                .zipcode("zipcode")
                .suite("suite")
                .lat(32.33)
                .lon(37.49)
                .name("Big place")
                .timezone("CST")
                .locationtype(LocationType.BUSINESS)
                .State("Arkansas").build();

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>(){});

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(3, results.getBody().size());
    }

    @Test
    public void testAddLocation() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        Location location = Location.builder()
                .id(Long.valueOf(1))
                .city("Fayetteville")
                .street("street")
                .zipcode("zipcode")
                .suite("suite")
                .lat(32.33)
                .lon(37.49)
                .name("Big place")
                .timezone("CST")
                .locationtype(LocationType.BUSINESS)
                .State("Arkansas").build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/save", location, String.class);

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>(){});

        locationList.add(0, location);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(locationList, results.getBody());
    }

    
}
