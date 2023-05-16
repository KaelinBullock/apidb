package com.example.apidb.company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping()
    public Optional<Company> getCompanyById(@RequestParam Long id) {
        return companyService.getCompany(id);
    }

    @CrossOrigin
    @GetMapping("/list")
    public Iterable<Company> getCompanies() {
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
