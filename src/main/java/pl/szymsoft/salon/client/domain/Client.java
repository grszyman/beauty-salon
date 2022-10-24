package pl.szymsoft.salon.client.domain;

import pl.szymsoft.salon.values.EmailAddress;
import pl.szymsoft.salon.values.FirstName;
import pl.szymsoft.salon.values.Gender;
import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.LastName;
import pl.szymsoft.salon.values.PhoneNumber;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static java.util.Objects.requireNonNullElseGet;

// Aggregate Root

@Getter
@ToString(doNotUseGetters = true)
public final class Client {

    private final Id<Client> id;
    private final FirstName firstName;
    private final LastName lastName;
    private final EmailAddress email;
    private final PhoneNumber phone;
    private final Gender gender;
    private final boolean banned;
    @Nullable
    private final Long version;

    @SuppressWarnings("ConstructorWithTooManyParameters")
    @Builder(toBuilder = true)
    private Client(
            @Nullable Id<Client> id,
            @NonNull FirstName firstName,
            @NonNull LastName lastName,
            @NonNull EmailAddress email,
            @NonNull PhoneNumber phone,
            @NonNull Gender gender,
            boolean banned,
            @Nullable Long version
    ) {
        this.id = requireNonNullElseGet(id, Id::random);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.banned = banned;
        this.version = version;
    }

    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }
}
