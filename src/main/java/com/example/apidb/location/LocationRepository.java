package com.example.apidb.location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {
    @Query(value = "SELECT l.* FROM Locations l WHERE l.location_name = ?1", nativeQuery = true)
    Iterable<Location> getLocationsByName(String name);
}
