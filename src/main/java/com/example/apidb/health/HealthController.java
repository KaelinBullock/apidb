package com.example.apidb.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthController {
    @GetMapping(value = "/")
    public ResponseEntity<String> healthCheck() {
        log.info("health");
        return ResponseEntity.ok().build();
    }
}