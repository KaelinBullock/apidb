package com.example.apidb.contact;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Iterable<Contact> getContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public Iterable<Contact> getContactByName(String name) {
        return contactRepository.getContactByName(name);
    }

    public Iterable<Contact> getContactByCompanyId(Long companyId) {
        return contactRepository.getContactByCompanyId(companyId);
    }

    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    public void save(List<Contact> contacts) {
        contactRepository.saveAll(contacts);
    }
}
