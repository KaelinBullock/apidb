package com.example.apidb.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@Entity
@Builder
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    Long id;
    String name;
    String street;
    String suite;
    String city;
    String State;
    String zipcode;
    TimeZone timeZone;
    LocationType locationType;
    double lat;
    double lon;

    public Location() {
    }
}