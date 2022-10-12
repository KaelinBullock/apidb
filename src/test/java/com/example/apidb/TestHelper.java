package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.contact.Contact;
import com.example.apidb.location.Location;
import com.example.apidb.location.LocationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@TestPropertySource("application.properties")
@EnableTransactionManagement
@ActiveProfiles("test")
public class TestHelper {

    static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    TestHelper() {

    }

    private static final Location testLocation = Location.builder()
            .id(1L)
            .city("Big city")
            .street("The street")
            .zipcode("338487")
            .suite("656")
            .lat(23.23)
            .lon(23.23)
            .name("USA place")
            .timeZone("CST")
            .locationType(LocationType.BUSINESS)
            .State("Big state").build();

    private static final Company testCompany = Company.builder()
            .id(1L)
            .name("Big Company")
            .location(testLocation).build();

    private static final Contact testContact = Contact.builder()
            .id(1L)
            .name("The guy from the gas station")
            .company(testCompany).build();
    public static Location getTestLocation() {
        return testLocation;
    }

    public static Contact getTestContact() {
        return testContact;
    }

    public static Company getTestCompany() {
        return testCompany;
    }
}
