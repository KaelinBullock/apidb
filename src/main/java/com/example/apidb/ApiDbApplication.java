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

	@Value("${json.contacts}")
	private String contactsJson;
	@Value("${json.locations}")
	private String locationsJson;
	@Value("${json.companies}")
	private String companiesJson;
	@Value("${json.shipments}")
	private String shipmentsJson;

	public static void main(String[] args) {
		SpringApplication.run(ApiDbApplication.class, args);
	}

	//make sure you clean up comments and todos
	//what users belong to a company
	//add error messages
	//might want a delete or deactivate

//	@Bean
//	CommandLineRunner runner(ContactService contactService, ShipmentService shipmentService,
//							 LocationService locationService, CompanyService companyService) {
//
//		return args -> {
//			// read JSON and load json
//			ObjectMapper mapper = new ObjectMapper()
//					.registerModule(new JavaTimeModule());
//
//			try {
//
//				TypeReference<List<Location>> locationList = new TypeReference<>() {};
//				InputStream inputStream = TypeReference.class.getResourceAsStream(locationsJson);
//				List<Location> locations = mapper.readValue(inputStream, locationList);
//				locationService.save(locations);
//
//				TypeReference<List<Company>> companyList = new TypeReference<>() {};
//				inputStream = TypeReference.class.getResourceAsStream(companiesJson);
//				List<Company> companies = mapper.readValue(inputStream, companyList);;
//				companyService.save(companies);
//
//				TypeReference<List<Contact>> contactList = new TypeReference<>() {};
//				inputStream = TypeReference.class.getResourceAsStream(contactsJson);
//				List<Contact> contacts = mapper.readValue(inputStream, contactList);
//				contactService.save(contacts);
//
//				TypeReference<List<Shipment>> shipmentList = new TypeReference<>() {};
//				inputStream = TypeReference.class.getResourceAsStream(shipmentsJson);
//				List<Shipment> shipments = mapper.readValue(inputStream, shipmentList);;
//				shipmentService.save(shipments);
//
//				System.out.println("Information Saved!");
//			} catch (IOException e){
//				System.out.println("Unable to save information: " + e.getMessage());
//			}
//		};
//	}
}
