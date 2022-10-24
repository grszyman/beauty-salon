package pl.szymsoft.salon.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import pl.szymsoft.salon.utils.Strings;

@EqualsAndHashCode
public final class FirstName {

    @JsonValue
    private final String value;

    private FirstName(String value) {
        this.value = Strings.requireNonBlank(value, "value");
    }

    @JsonCreator
    public static FirstName of(String value) {
        return new FirstName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
