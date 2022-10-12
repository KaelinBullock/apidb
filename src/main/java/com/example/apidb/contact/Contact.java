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
public class Contact {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    Long id;
    String name;
    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "company_id",
            referencedColumnName = "id"
    )
    private Company company;

    public Contact() {
    }
}
