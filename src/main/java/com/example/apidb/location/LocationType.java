package com.example.apidb.location;

import java.util.Arrays;
import java.util.Optional;

public enum LocationType {
    BUSINESS("BUSINESS"),
    RESIDENTIAL("RESIDENTIAL"),
    STORAGE("STORAGE");

    private String type;

    LocationType (String type) {
        type = type;
    }


    public String getType() {
        return this.type;
    }

    public static Optional<LocationType> fromText(String type) {
        return Arrays.stream(values())
                .filter(t -> t.type.equalsIgnoreCase(type))
                .findFirst();
    }
}
