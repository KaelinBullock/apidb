package com.example.apidb.company;

import com.example.apidb.location.Location;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "companies")
@Builder
public class Company {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
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