package pl.szymsoft.salon.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import org.apache.commons.validator.routines.EmailValidator;
import pl.szymsoft.salon.utils.Objects;

@EqualsAndHashCode
public final class EmailAddress {

    private static final EmailValidator validator = EmailValidator.getInstance();

    @JsonValue
    private final String value;

    private EmailAddress(String value) {
        this.value = Objects.require(value, validator::isValid, "'" + value + "' is not a valid email address");
    }

    @JsonCreator
    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }

    @Override
    public String toString() {
        return value;
    }
}