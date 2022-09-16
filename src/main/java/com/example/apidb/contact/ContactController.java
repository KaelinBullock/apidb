package com.example.apidb.contact;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/contact")
@Slf4j
public class ContactController {

    ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/list")
    public Iterable<Contact> getContacts(@RequestParam Long id) {
        return contactService.getContacts();
    }

    @GetMapping("/getContactsById")
    public Optional<Contact> getContactById(@RequestParam Long id) {
        return contactService.getContactById(id);
    }

    @GetMapping("/getContactsByName")
    public Iterable<Contact> getContactsByName(@RequestParam String name) {
        return contactService.getContactByName(name);
    }

    @GetMapping("/getContactsByCompanyId")
    public Iterable<Contact> getContactsByName(@RequestParam Long companyId) {
        return contactService.getContactByCompanyId(companyId);
    }

    @PostMapping("/save")
    public void save(@RequestBody Contact contact) {
        contactService.save(contact);
    }
}
