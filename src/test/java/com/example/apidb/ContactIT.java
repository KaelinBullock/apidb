package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.Contact;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.Location;
import com.example.apidb.location.LocationRepository;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.example.apidb.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactIT {
    @Value("${json.contacts}")
    private String contactsJson;

    @MockBean
    ShipmentRepository shipmentRepository;

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    ObjectMapper mapper = new ObjectMapper();
    List<Contact> contactList;

    @BeforeEach
    public void setup() {//make sure this only run once
        TypeReference<List<Contact>> companies = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(contactsJson);
        try {
            contactList = mapper.readValue(inputStream, companies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getContactByIdTest() {
        TypeReference<List<Contact>> contacts = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(contactsJson);
        try {
            contactList = mapper.readValue(inputStream, contacts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Contact> results = restTemplate.exchange("http://localhost:" + port + "/api/contact?id=99",
                HttpMethod.GET,
                null,
                Contact.class);

        assertEquals(HttpStatus.OK, results.getStatusCode());
        Assert.assertNotNull(results);
        assertEquals(contactList.get(2), results.getBody());
    }

    @Test
    public void getContactsTest() {
        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port + "/api/contact/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertNotNull(results);
        assertEquals(3, results.getBody().size());
    }

    @Test
    public void saveContactTest() {

        Contact contact = Contact.builder()
                .id(Long.valueOf(1))
                .company(getTestCompany())
                .name("quint").build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/contact/save", contact, String.class);

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port + "/api/contact/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});


        contactList.add(0, contact);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(4, results.getBody().size());
        assertEquals(contactList, results.getBody());
    }//how can i make this not order dependent.
    //I could delete the entry
    //i couldn't seaerch for the specific contact, because the id could be different,so thats an issue

    @Test
    public void saveContact_nullValuesTest() {
        Contact contact = Contact.builder().id(Long.valueOf(1)).build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/contact/save", contact, String.class);

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port + "/api/contact/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});


        contactList.add(0, contact);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(4, results.getBody().size());
        assertEquals(contactList, results.getBody());
    }
}
