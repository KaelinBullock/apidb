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

import static java.lang.Long.*;

@Configuration
@TestPropertySource("application.properties")
@EnableTransactionManagement
@ActiveProfiles("test")
public class TestHelper {
    //have it read in the json values into lists instead of setting these like this here

    static ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    TestHelper() {

    }

    private static final Location testLocation = Location.builder()
            .id(99L)
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
            .id(valueOf(99))
            .name("Big Company")
            .location(testLocation).build();//why does saving a null location effect location 99

    private static final Contact testContact = Contact.builder()
            .id(99L)
            .name("Jarule")
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
