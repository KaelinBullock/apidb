package com.example.apidb;

import com.example.apidb.company.CompanyController;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.company.CompanyService;
import com.example.apidb.contact.Contact;
import com.example.apidb.contact.ContactController;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.contact.ContactService;
import com.example.apidb.location.*;
import com.example.apidb.shipment.Shipment;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiDbApplication.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource("classpath:/application.properties")
@ActiveProfiles("test")
@Slf4j
public class ShipmentITTests {

    @Autowired
    Environment environment;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ShipmentRepository shipmentRepository;
    @MockBean
    private CompanyController companyController;
    @MockBean
    private CompanyService companyService;
    @MockBean
    CompanyRepository companyRepository;
    @MockBean
    ContactRepository contactRepository;
    @MockBean
    ContactService contactService;
    @MockBean
    ContactController contactController;
    @MockBean
    LocationService locationService;
    @MockBean
    LocationController locationController;
    @MockBean
    LocationRepository locationRepository;

    //make sure it doesn't use real data.  Try to get rid of some of these mocks if you can

//    @Test
//    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
//        Shipment shipment = Shipment.builder().build();
//        shipmentRepository
//                .save(shipment);
//        Long id = Long.valueOf(1);
//        Shipment result = (Shipment) shipmentRepository.find(id).get();
//
//        assertNotNull(result);
//        assertEquals(shipment, result);
//    }

    @Test
    public void testAddLocation() {
        String port = environment.getProperty("local.server.port");
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        TypeReference<List<Contact>> contactList = new TypeReference<List<Contact>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/contacts.json");
        List<Contact> contacts;
        try {
            contacts = mapper.readValue(inputStream, contactList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
        Location location = Location.builder()
                .id(Long.valueOf(7))
                .city("Fayetteville")
                .lat(32.33)
                .lon(37.49)
                .locationType(LocationType.BUSINESS)
                .name("Big place")
                .State("Arkansas").build();



        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/location/save", location, String.class);

        ResponseEntity<List<Location>> results = restTemplate.exchange("http://localhost:" + port + "/api/location/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>(){});

//        ResponseEntity<Location[]> locations = this.restTemplate
//                .getForEntity("http://localhost:" + port + "/api/contact/list", Location[].class);
        log.info(String.valueOf(results));
//        log.info(String.valueOf(contacts));
        log.info("");
//        assertEquals(201, responseEntity.getStatusCodeValue());
    }


//    @Test
//    public void testAddShipment() {
//        String port = environment.getProperty("local.server.port");
//
//        Location location = Location.builder().id(Long.valueOf(7)).build();
//        Contact contact = Contact.builder().id(Long.valueOf(4)).build();
//        LocalDate localDate = LocalDate.of(2022, 11,3);
//        Shipment shipment = Shipment.builder()
//                .deliveryDate(localDate)
//                .location(location)
//                .contact(contact)
//                .creationDate(localDate).build();
//
////        ResponseEntity<String> responseEntity = this.restTemplate
////                .postForEntity("http://localhost:" + port + "/api/shipment/save", shipment, String.class);
////        ResponseEntity<Shipment[]> result = this.restTemplate
////                .getForEntity("http://localhost:" + port + "/api/shipment/list", Shipment[].class);
//        ResponseEntity<Location[]> locations = this.restTemplate
//                .getForEntity("http://localhost:" + port + "/api/location/list", Location[].class);
//        ResponseEntity<Contact[]> contacts = this.restTemplate
//                .getForEntity("http://localhost:" + port + "/api/contact/list", Contact[].class);
//        log.info(String.valueOf(locations));
//        log.info(String.valueOf(contacts));
//        log.info("");
////        assertEquals(201, responseEntity.getStatusCodeValue());
//    }


}
