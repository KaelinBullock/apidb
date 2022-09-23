package com.example.apidb.contact;

import com.example.apidb.company.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Builder
@Table(name = "contacts")
public class Contact {//TODO go back and add underscores to db tables with two words
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    Long id;
    String name;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "company_id",
            referencedColumnName = "id"
    )
    private Company company;

    public Contact() {
    }
}
