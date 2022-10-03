package com.example.apidb;

import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.Contact;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.*;
import com.example.apidb.shipment.Shipment;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.apidb.TestHelper.getTestContact;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShipmentIT {

    @Value("${json.shipments}")
    String shipmentsJson;

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
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
    List<Shipment> shipmentList = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper()
					.registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() {

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port + "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        shipmentList = results.getBody();
        TypeReference<List<Shipment>> shipments = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(shipmentsJson);
        try {
            shipmentList = mapper.readValue(inputStream, shipments);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetShipment() {

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        ResponseEntity<Shipment> results = restTemplate.exchange("http://localhost:" + port + "/api/shipment?id=99",
                HttpMethod.GET,
                null,
                Shipment.class);

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(shipmentList.get(2), results.getBody());
    }

    @Test
    public void testGetShipments() {

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port + "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(3, results.getBody().size());
    }

    @Test
    public void testSaveShipment() {

        Shipment shipment = Shipment.builder()
                .id(Long.valueOf(100))
                .contact(getTestContact())
                .deliveryDate(LocalDate.now())
                .creationDate(LocalDate.of(11, 11, 11))
                .build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/shipment/save", shipment, String.class);

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port + "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        shipmentList.add(shipment);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(4, results.getBody().size());
        assertThat(results.getBody(), samePropertyValuesAs(shipmentList));
    }
    @Test
    public void testSaveShipment_nullValues() {

        Shipment shipment = Shipment.builder().id(Long.valueOf(100))
                .build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/shipment/save", shipment, String.class);

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port + "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        shipmentList.add(shipment);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(4, results.getBody().size());
        assertEquals(shipmentList, results.getBody());
    }

}
