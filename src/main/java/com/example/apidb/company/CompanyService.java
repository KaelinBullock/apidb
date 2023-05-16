package com.example.apidb.company;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;


    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Iterable<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public Iterable<Company> getCompaniesByName(String name) {
        return companyRepository.getCompaniesByName(name);
    }

    public Optional<Company> getCompany(Long id) {
        return companyRepository.findById(id);
    }//TODO if a new location is sent in save it

    public void save(Company company) {
        companyRepository.save(company);
    }

    public void save(List<Company> companies) {
        companyRepository.saveAll(companies);
    }
}
