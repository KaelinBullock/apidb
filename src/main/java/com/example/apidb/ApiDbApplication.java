package com.example.apidb;

import com.example.apidb.company.Company;
import com.example.apidb.company.CompanyService;
import com.example.apidb.contact.Contact;
import com.example.apidb.location.Location;
import com.example.apidb.shipment.Shipment;
import com.example.apidb.contact.ContactService;
import com.example.apidb.location.LocationService;
import com.example.apidb.shipment.ShipmentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class ApiDbApplication {

//	@Value("${json.contact}")
//	private String contactsJson;

	public static void main(String[] args) {
		SpringApplication.run(ApiDbApplication.class, args);
	}

	//write tests
	//make sure you clean up cocmments and todos
	//think of things customers might want to query.
	//what users belong to a company
	//add error messages
	@Bean
	CommandLineRunner runner(ContactService contactService, ShipmentService shipmentService,
							 LocationService locationService, CompanyService companyService) {
		return args -> {
			// read JSON and load json
			//TODO put the file paths in apllication.properties
			ObjectMapper mapper = new ObjectMapper()
					.registerModule(new JavaTimeModule());

			try {
				TypeReference<List<Contact>> contactList = new TypeReference<List<Contact>>(){};
				InputStream inputStream = TypeReference.class.getResourceAsStream("/json/contacts.json");
				List<Contact> contacts = mapper.readValue(inputStream, contactList);
				contactService.save(contacts);



				TypeReference<List<Location>> locationList = new TypeReference<List<Location>>(){};
				inputStream = TypeReference.class.getResourceAsStream("/json/locations.json");
				List<Location> locations = mapper.readValue(inputStream, locationList);
				locationService.save(locations);

				TypeReference<List<Company>> companyList = new TypeReference<List<Company>>(){};
				inputStream = TypeReference.class.getResourceAsStream("/json/companies.json");
				List<Company> companies = mapper.readValue(inputStream, companyList);;
				companyService.save(companies);

				//need to fix for tests
//				TypeReference<List<Shipment>> shipmentList = new TypeReference<List<Shipment>>(){};
//				inputStream = TypeReference.class.getResourceAsStream("/json/shipments.json");
//				List<Shipment> shipments = mapper.readValue(inputStream, shipmentList);;
//				shipmentService.save(shipments);

				System.out.println("Users Saved!");
			} catch (IOException e){
				System.out.println("Unable to save users: " + e.getMessage());
			}
		};
	}
}
