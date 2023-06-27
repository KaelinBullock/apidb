package com.example.apidb.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Iterable<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public Iterable<Company> getCompaniesByName(String name) {
        return companyRepository.getCompaniesByName(name);
    }

    public Optional<Company> getCompany(Long id) {
        return companyRepository.findById(id);
    }

    public void save(Company company) {
        companyRepository.save(company);
    }

    public void save(List<Company> companies) {
        companyRepository.saveAll(companies);
    }
}
