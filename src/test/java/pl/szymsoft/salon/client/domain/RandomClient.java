package pl.szymsoft.salon.client.domain;

import pl.szymsoft.salon.values.EmailAddress;
import pl.szymsoft.salon.values.FirstName;
import pl.szymsoft.salon.values.Gender;
import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.LastName;
import pl.szymsoft.salon.values.PhoneNumber;
import pl.szymsoft.salon.values.RandomEmailAddress;
import pl.szymsoft.salon.values.RandomFirstName;
import pl.szymsoft.salon.values.RandomLastName;
import pl.szymsoft.salon.values.RandomPhoneNumber;
import lombok.Builder;
import net.andreinc.mockneat.unit.types.Bools;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static net.andreinc.mockneat.unit.objects.From.from;

public final class RandomClient {

    private final Client client;

    public static RandomClientBuilder randomClient() {
        return new RandomClientBuilder();
    }

    @SuppressWarnings({"PublicMethodNotExposedInInterface", "PublicInnerClass"})
    public static class RandomClientBuilder {

        public Client toClient() {
            return build().client;
        }
    }

    @Builder
    private RandomClient(
            @Nullable Id<Client> id,
            @Nullable String firstName,
            @Nullable String lastName,
            @Nullable String email,
            @Nullable String phone,
            @Nullable Gender gender,
            @Nullable Boolean banned,
            @Nullable Long version
    ) {
        //noinspection AutoUnboxing
        client = Client.builder()
                .id(id)
                .firstName(Optional.ofNullable(firstName)
                        .map(FirstName::of)
                        .orElseGet(RandomFirstName::get))
                .lastName(Optional.ofNullable(lastName)
                        .map(LastName::of)
                        .orElseGet(RandomLastName::get))
                .email(Optional.ofNullable(email)
                        .map(EmailAddress::of)
                        .orElseGet(RandomEmailAddress::get))
                .phone(Optional.ofNullable(phone)
                        .map(PhoneNumber::of)
                        .orElseGet(RandomPhoneNumber::get))
                .gender(Optional.ofNullable(gender)
                        .orElseGet(() -> from(Gender.class).get()))
                .banned(Optional.ofNullable(banned)
                        .orElseGet(Bools.bools()::get))
                .version(version)
                .build();
    }

}
