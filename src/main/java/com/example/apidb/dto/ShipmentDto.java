package com.example.apidb.dto;

import com.example.apidb.contact.Contact;
import com.example.apidb.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ShipmentDto {
    private Long id;
    private LocalDate creationDate;
    private LocalDate deliveryDate;


    private Location location;


    private Contact contact;
}
