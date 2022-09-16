package com.example.apidb.shipment;

import com.example.apidb.contact.Contact;
import com.example.apidb.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private LocalDate creationDate;
    private LocalDate deliveryDate;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "id"
    )
    private Location location;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "contact_id",
            referencedColumnName = "id"
    )
    private Contact contact;


    public Shipment() {

    }
}
