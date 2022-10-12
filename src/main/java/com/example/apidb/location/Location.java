package com.example.apidb.location;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Builder
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    Long id;
    String name;
    String street;
    String suite;
    String city;
    String State;
    String zipcode;
    @Column(name="time_zone")
    String timeZone;
    @Enumerated(EnumType.STRING)
    @Column(name="location_type")
    LocationType locationType;
    double lat;
    double lon;

    public Location() {
    }
}