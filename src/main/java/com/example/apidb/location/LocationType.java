package com.example.apidb.location;

import java.util.Arrays;
import java.util.Optional;

public enum LocationType {
    BUSINESS("BUSINESS"),
    RESIDENTIAL("RESIDENTIAL"),
    STORAGE("STORAGE");

    public final String type;

    LocationType (String type) {
        this.type = type;
    }

    public static Optional<LocationType> fromText(String type) {
        return Arrays.stream(values())
                .filter(t -> t.type.equalsIgnoreCase(type))
                .findFirst();
    }
}
