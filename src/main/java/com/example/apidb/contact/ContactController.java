package com.example.apidb.contact;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
@Slf4j
public class ContactController {

    final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping()
    public Optional<Contact> getContactById(@RequestParam Long id) {
        return contactService.getContactById(id);
    }
    @GetMapping("/list")
    public Iterable<Contact> getContacts() {
        return contactService.getContacts();
    }

    @GetMapping("/getContactsByName")
    public Iterable<Contact> getContactsByName(@RequestParam String name) {
        return contactService.getContactByName(name);
    }

    @GetMapping("/getContactsByCompanyId")
    public Iterable<Contact> getContactsByName(@RequestParam Long id) {
        return contactService.getContactByCompanyId(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody Contact contact) {
        contactService.save(contact);
    }

    @PostMapping("/saveAll")
    public void save(@RequestBody List<Contact> contacts) {
        contactService.save(contacts);
    }
}
