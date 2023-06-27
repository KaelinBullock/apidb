package com.example.apidb.company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
@Slf4j
@CrossOrigin
public class CompanyController {

    final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping()
    public Optional<Company> getCompanyById(@RequestParam Long id) {
        return companyService.getCompany(id);
    }

    @GetMapping("/list")
    public Iterable<Company> getCompanies() {
        log.info("Get Companies");
        return companyService.getCompanies();
    }

    @GetMapping("/getCompaniesByName")
    public Iterable<Company> getCompaniesByName(@RequestParam String name) {
        return companyService.getCompaniesByName(name);
    }

    @PostMapping("/save")
    public void save(@RequestBody Company company) {
        companyService.save(company);
    }

    @PostMapping("/saveAll")
    public void save(@RequestBody List<Company> companies) {
        companyService.save(companies);
    }
}
