package pl.szymsoft.salon.values;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

    MALE("Male"), FEMALE("Female");

    private final String asString;

    Gender(String asString) {
        this.asString = asString;
    }

    @JsonValue
    @Override
    public String toString() {
        return asString;
    }
}
