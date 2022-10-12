package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.company.CompanyRepository;
import com.example.apidb.contact.ContactRepository;
import com.example.apidb.location.Location;
import com.example.apidb.location.LocationRepository;
import com.example.apidb.shipment.ShipmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Collections;
import java.util.List;

import static com.example.apidb.TestHelper.getTestLocation;
import static com.example.apidb.TestHelper.mapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

        ResponseEntity<Company> results = restTemplate.exchange("http://localhost:" + port + "/api/company?id=3",
                HttpMethod.GET,
                null,
                Company.class);

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(companyList.get(2)).usingRecursiveComparison().isEqualTo(results.getBody());
    }

    @Test
    public void getCompanyByNameTest() {

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port +
                        "/api/company/getCompaniesByName?name=Big Company",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});

        assertNotNull(results);
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(Collections.singletonList(companyList.get(0))).usingRecursiveComparison().isEqualTo(results.getBody());
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
        assertFalse(results.getBody().isEmpty());
    }

    @Test
    public void testSaveCompany() {

        Company company = Company.builder()
                .name("Another Corp")
                .location(getTestLocation()).build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/company/save", company, String.class);

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port + "/api/company/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Company> resultBody = results.getBody();
        company.setId(Long.valueOf(resultBody.size()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(company);
    }
    @Test
    public void testSaveCompany_nullValues() {

        Company company = Company.builder().build();

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/company/save", company, String.class);

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port + "/api/company/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });


        List<Company> resultBody = results.getBody();
        company.setId((long) resultBody.size());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.get(resultBody.size() - 1)).usingRecursiveComparison().isEqualTo(company);
    }

    @Test
    public void testSaveAllCompanies() {

        List<Company> companies = new ArrayList<>();
        companies.add(Company.builder().name("wiggle")
                .location(Location.builder().id(1L).build()).build());
        companies.add(Company.builder().name("wobble").build());
        companies.add(Company.builder().name("knobble").build());

        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/company/saveAll", companies, String.class);

        ResponseEntity<List<Company>> results = restTemplate.exchange("http://localhost:" + port + "/api/company/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<Company> resultBody = results.getBody();
        int bodySize = resultBody.size();
        companies.get(0).setId((long) (bodySize - 2));
        companies.get(0).setLocation(getTestLocation());
        companies.get(1).setId((long) (bodySize - 1));
        companies.get(2).setId((long) (bodySize));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertThat(resultBody.subList(bodySize - 3, bodySize)).usingRecursiveComparison().isEqualTo(companies);
    }
}
