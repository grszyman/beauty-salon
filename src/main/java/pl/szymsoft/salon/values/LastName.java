package pl.szymsoft.salon.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import pl.szymsoft.salon.utils.Strings;

@EqualsAndHashCode
public final class LastName {

    @JsonValue
    private final String value;

    private LastName(String value) {
        this.value = Strings.requireNonBlank(value, "value");
    }

    @JsonCreator
    public static LastName of(String value) {
        return new LastName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
