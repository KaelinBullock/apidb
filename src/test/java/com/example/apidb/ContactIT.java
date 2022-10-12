package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.Contact;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.LocationRepository;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apidb.TestHelper.getTestCompany;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    private final ObjectMapper mapper = new ObjectMapper();
    List<Contact> contactList;

    @BeforeEach
    public void setup() {
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

        ResponseEntity<Contact> results = restTemplate.exchange("http://localhost:" + port + "/api/contact?id=1",
                HttpMethod.GET,
                null,
                Contact.class);

        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertNotNull(results);
        assertThat(contactList.get(0)).usingRecursiveComparison().isEqualTo(results.getBody());
    }

    @Test
    public void getContactByNameTest() {

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/contact/getContactsByName?name=The guy from the gas station",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(Collections.singletonList(contactList.get(0))).usingRecursiveComparison().isEqualTo(results.getBody());
    }

    @Test
    public void getContactsByCompanyIdTest() {

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/contact/getContactsByCompanyId?id=2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(Collections.singletonList(contactList.get(1))).usingRecursiveComparison().isEqualTo(results.getBody());
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
                .company(getTestCompany())
                .name("quint").build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/contact/save", contact, String.class);

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port + "/api/contact/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});

        List<Contact> resultBody = results.getBody();
        assert resultBody != null;
        contact.setId((long) resultBody.size());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(contact);
    }

    @Test
    public void saveContact_nullValuesTest() {
        Contact contact = Contact.builder().build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/contact/save", contact, String.class);

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port + "/api/contact/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});

        List<Contact> resultBody = results.getBody();
        assert resultBody != null;
        contact.setId((long) resultBody.size());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(contact);
    }

    @Test
    public void testSaveAllContacts() {

        List<Contact> contacts = new ArrayList<>();
        contacts.add(Contact.builder().name("wiggle")
                .company(Company.builder().id(1L).build()).build());
        contacts.add(Contact.builder().name("wobble").build());
        contacts.add(Contact.builder().name("knobble").build());

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/contact/saveAll", contacts, String.class);

        ResponseEntity<List<Contact>> results = restTemplate.exchange("http://localhost:" + port + "/api/contact/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Contact> resultBody = results.getBody();
        assert resultBody != null;
        int bodySize = resultBody.size();
        contacts.get(0).setId((long) (bodySize - 2));
        contacts.get(0).setCompany(getTestCompany());
        contacts.get(1).setId((long) (bodySize - 1));
        contacts.get(2).setId((long) (bodySize));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.subList(bodySize - 3, bodySize)).usingRecursiveComparison().isEqualTo(contacts);
    }
}
