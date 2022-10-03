package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.LocationRepository;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.BeforeClass;
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
import java.util.List;

import static com.example.apidb.TestHelper.getTestLocation;
import static com.example.apidb.TestHelper.mapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyIT {

    @Value("${json.companies}")
    private String companiesJson;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    LocationRepository locationRepository;

    @MockBean
    ShipmentRepository shipmentRepository;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();
    List<Company> companyList;

    @BeforeEach
    public void setup() {
        TypeReference<List<Company>> companies = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(companiesJson);
        try {
            companyList = mapper.readValue(inputStream, companies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getCompanyTest() {

        ResponseEntity<Company> results = restTemplate.exchange("http://localhost:" + port + "/api/company?id=99",
                HttpMethod.GET,
                null,
                Company.class);

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(companyList.get(2), results.getBody());
    }

    @Test
    public void getCompaniesTest() {

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port + "/api/company/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(companyList, results.getBody());
    }

    @Test
    public void testSaveCompany() {

        Company company = Company.builder()
                .id(1L)
                .name("Another Corp")
                .location(getTestLocation()).build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/company/save", company, String.class);

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port + "/api/company/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        companyList.add(0, company);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(4, results.getBody().size());
        assertEquals(companyList, results.getBody());
    }
    @Test
    public void testSaveCompany_nullValues() {

        Company company = Company.builder().id(Long.valueOf(2)).build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/company/save", company, String.class);

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port + "/api/company/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        Company company1 = Company.builder()
                .id(Long.valueOf(1))
                .name("Another Corp")
                .location(getTestLocation()).build();
        companyList.add(0, company1);//there might be a better way than this maybe putting them all in and removing one or changing the order, or only using the json to confirm the one test that needs to do that

        companyList.add(1, company);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(5, results.getBody().size());
        assertEquals(companyList, results.getBody());
    }
}
