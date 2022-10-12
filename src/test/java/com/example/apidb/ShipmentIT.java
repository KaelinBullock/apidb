package com.example.apidb;

import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.Contact;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.*;
import com.example.apidb.shipment.Shipment;
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
import org.springframework.http.*;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apidb.TestHelper.getTestContact;
import static com.example.apidb.TestHelper.mapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @BeforeEach
    public void setup() {
        TypeReference<List<Shipment>> shipments = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(shipmentsJson);
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        headers.setContentType(MediaType.APPLICATION_JSON);

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

        ResponseEntity<Shipment> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment?id=1",
                HttpMethod.GET,
                null,
                Shipment.class);

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(results.getBody()).usingRecursiveComparison().isEqualTo(shipmentList.get(0));
    }

    @Test
    public void testGetShipments() {

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertFalse(results.getBody().isEmpty());
    }

    @Test
    public void testGetShipmentByLocationId() {

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment/getShipmentByLocationId?id=2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(results.getBody()).usingRecursiveComparison()
                .isEqualTo(Collections.singletonList(shipmentList.get(1)));
    }

    @Test
    public void testGetShipmentsByContactId() {

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment/getShipmentsByContactId?id=2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(results.getBody()).usingRecursiveComparison()
                .isEqualTo(Collections.singletonList(shipmentList.get(1)));
    }

    @Test
    public void testSaveShipment() {

        Shipment shipment = Shipment.builder()
                .contact(getTestContact())
                .deliveryDate(LocalDate.now())
                .creationDate(LocalDate.of(2023, 11, 20))
                .build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/shipment/save", shipment, String.class);

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Shipment> resultBody = results.getBody();
        shipment.setId(Long.valueOf(resultBody.size()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(shipment);
    }
    @Test
    public void testSaveShipment_nullValues() {
        Shipment shipment = Shipment.builder().build();

        HttpEntity<Shipment> request = new HttpEntity<>(shipment, headers);

        ResponseEntity<String> responseEntity  =
                restTemplate.postForEntity("http://localhost:" + port + "/api/shipment/save", request, String.class);

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Shipment> resultBody = results.getBody();
        shipment.setId(Long.valueOf(resultBody.size()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(shipment);
    }

    @Test
    public void testSaveAllShipments() {

        List<Shipment> shipments = new ArrayList<>();
        shipments.add(Shipment.builder().creationDate(LocalDate.now()).deliveryDate(LocalDate.now())
                .contact(Contact.builder().id(1L).build()).build());
        shipments.add(Shipment.builder().build());
        shipments.add(Shipment.builder().build());

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/shipment/saveAll", shipments, String.class);

        ResponseEntity<List<Shipment>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/shipment/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Shipment> resultBody = results.getBody();
        int bodySize = resultBody.size();
        Shipment shipment = shipments.get(0);
        shipment.setId((long) (bodySize - 2));
        shipment.setContact(getTestContact());
        shipments.get(1).setId((long) (bodySize - 1));
        shipments.get(2).setId((long) (bodySize));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.subList(bodySize - 3, bodySize)).usingRecursiveComparison().isEqualTo(shipments);
    }
}
