package com.example.apidb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@SpringBootApplication
public class ApiDbApplication {
	//TODO emails
	public static void main(String[] args) {
		SpringApplication.run(ApiDbApplication.class, args);
	}

}
