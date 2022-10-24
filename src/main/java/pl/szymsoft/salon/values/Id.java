package pl.szymsoft.salon.values;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
public final class Id<T> {

    @NonNull
    private final UUID id;

    public static <T> Id<T> of(String value) {
        return new Id<>(UUID.fromString(value));
    }

    public static <T> Id<T> of(UUID value) {
        return new Id<>(value);
    }

    public static <T> Id<T> random() {
        return new Id<>(randomUUID());
    }

    public UUID toUUID() {
        return id;
    }

    @JsonValue
    @Override
    public String toString() {
        return id.toString();
    }
}
