package com.example.apidb.company;

import com.example.apidb.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    @Column( name = "company_name")
    private String name;

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "id"
    )
    private Location location;

    public Company() {}
}