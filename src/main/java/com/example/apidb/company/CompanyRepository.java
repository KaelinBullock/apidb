package com.example.apidb.company;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

    @Query(value = "SELECT c.* FROM Companies c WHERE c.company_name = ?1", nativeQuery = true)
    Iterable<Company> getCompaniesByName(String name);
}
