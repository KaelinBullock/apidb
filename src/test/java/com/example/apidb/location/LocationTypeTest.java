package com.example.apidb.location;

import org.junit.jupiter.api.Test;

import static com.example.apidb.location.LocationType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTypeTest {

    @Test
    public void fromTextTest() {
        assertEquals(fromText("BUSINESS").get(), BUSINESS);
        assertEquals(fromText("RESIDENTIAL").get(), RESIDENTIAL);
        assertEquals(fromText("STORAGE").get(), STORAGE);
    }
}
