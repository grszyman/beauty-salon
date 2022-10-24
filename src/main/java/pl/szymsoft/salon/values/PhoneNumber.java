package pl.szymsoft.salon.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import pl.szymsoft.salon.utils.Strings;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;

@EqualsAndHashCode
public final class PhoneNumber {

    // Based on phone numbers from clients.csv
    public static final String REGEX = "^(?:(1)[- .]?)?(?:\\((\\d{3})\\)|(\\d{3}))[- .]?(\\d{3})[- .]?(\\d{4})$";
    public static final String MESSAGE = "must contain 10 digits or 11 digits starting with 1";

    private static final Pattern pattern = compile(REGEX);

    private final String normalisedEntry;

    private PhoneNumber(String value) {

        //noinspection CallToSuspiciousStringMethod
        var trimmedValue = Strings.requireNonBlank(value, "value").trim();
        var matcher = pattern.matcher(trimmedValue);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(value + " is not a valid phone number");
        }

        normalisedEntry = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(matcher::group)
                .filter(Objects::nonNull)
                .collect(joining(" "));
    }

    @JsonCreator
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return normalisedEntry;
    }
}
