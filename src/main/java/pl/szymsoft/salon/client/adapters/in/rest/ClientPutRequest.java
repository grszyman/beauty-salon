package pl.szymsoft.salon.client.adapters.in.rest;

import pl.szymsoft.salon.client.adapters.in.rest.validators.Phone;
import pl.szymsoft.salon.client.adapters.in.rest.validators.ValueOfEnum;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.EmailAddress;
import pl.szymsoft.salon.values.FirstName;
import pl.szymsoft.salon.values.Gender;
import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.LastName;
import pl.szymsoft.salon.values.PhoneNumber;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Locale;

import static java.lang.Boolean.parseBoolean;

// Spring can handle boolean, enums and even value objects, but that way we can produce a nicer and complete error message.
record ClientPutRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NonNull
        @Email
        String email,

        @NotNull
        @Phone
        String phone,

        @NotNull
        @ValueOfEnum(enumClass = Gender.class, message = "must be a valid gender")
        String gender,

        @Pattern(regexp = "^true$|^false$", message = "must be either true or false")
        String banned
) {
    Client toClient(String id) {
        return toClient(id, null);
    }

    Client toClient(String id, @Nullable Long version) {
        return Client.builder()
                .id(Id.of(id))
                .firstName(FirstName.of(firstName))
                .lastName(LastName.of(lastName))
                .email(EmailAddress.of(email))
                .phone(PhoneNumber.of(phone))
                .gender(Gender.valueOf(gender.toUpperCase(Locale.ROOT)))
                .banned(parseBoolean(banned))
                .version(version)
                .build();
    }
}
