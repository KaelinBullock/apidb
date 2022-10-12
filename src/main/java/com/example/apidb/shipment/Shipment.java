package com.example.apidb.shipment;

import com.example.apidb.contact.Contact;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(name="creation_date")
    private LocalDate creationDate;
    @Column(name="delivery_date")
    private LocalDate deliveryDate;

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
