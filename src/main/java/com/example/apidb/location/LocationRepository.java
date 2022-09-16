package com.example.apidb.location;

import com.example.apidb.location.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {
}
