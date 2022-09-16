package com.example.apidb.contact;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long> {

    @Query(value = "SELECT c FROM Contacts c WHERE c.name = ?1", nativeQuery = true)
    Iterable<Contact> getContactByName(String name);

    @Query(value = "SELECT c FROM Contacts c WHERE c.company_id = ?1", nativeQuery = true)
    Iterable<Contact> getContactByCompanyId(Long companyId);
}
