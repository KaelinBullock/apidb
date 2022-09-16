package com.example.apidb.company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/")
    public Optional<Company> getCompanyById(@RequestParam Long id) {
        return companyService.getCompany(id);
    }

    @GetMapping("/list")
    public Iterable<Company> getCompanies() {
        return companyService.getCompanies();
    }

    @GetMapping("/getCompaniesByName")
    public Iterable<Company> getCompaniesByName(String companyName) {
        return companyService.getCompaniesByName(companyName);
    }

    @PostMapping("/save")
    public void save(@RequestBody Company company) {
        companyService.save(company);
    }
}
