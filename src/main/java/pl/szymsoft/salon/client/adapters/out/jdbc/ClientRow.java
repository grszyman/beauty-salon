package pl.szymsoft.salon.client.adapters.out.jdbc;

import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.EmailAddress;
import pl.szymsoft.salon.values.FirstName;
import pl.szymsoft.salon.values.Gender;
import pl.szymsoft.salon.values.LastName;
import pl.szymsoft.salon.values.PhoneNumber;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Table(ClientsJdbcAdapterSpringConfiguration.CLIENT_TABLE)
record ClientRow(
        @Id UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Gender gender,
        boolean banned,
        @Nullable @Version Long version
) {
    static ClientRow from(Client client) {
        return new ClientRow(
                client.getId().toUUID(),
                client.getFirstName().toString(),
                client.getLastName().toString(),
                client.getEmail().toString(),
                client.getPhone().toString(),
                client.getGender(),
                client.isBanned(),
                client.getVersion().orElse(null)
        );
    }

    Client toClient() {
        return Client.builder()
                .id(pl.szymsoft.salon.values.Id.of(id))
                .firstName(FirstName.of(firstName))
                .lastName(LastName.of(lastName))
                .email(EmailAddress.of(email))
                .phone(PhoneNumber.of(phone))
                .gender(gender)
                .version(version)
                .build();
    }
}
