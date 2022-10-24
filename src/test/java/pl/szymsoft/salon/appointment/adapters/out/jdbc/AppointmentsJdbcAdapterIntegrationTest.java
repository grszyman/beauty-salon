package pl.szymsoft.salon.appointment.adapters.out.jdbc;

import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.appointment.domain.ports.AppointmentsSpec;
import pl.szymsoft.salon.client.adapters.out.jdbc.ClientsJdbcAdapterSpringConfiguration;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.jdbc.JdbcTableCleanerExtension;
import pl.szymsoft.salon.values.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static pl.szymsoft.salon.client.adapters.out.jdbc.ClientsJdbcAdapterSpringConfiguration.CLIENT_TABLE;
import static pl.szymsoft.salon.client.domain.RandomClient.randomClient;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * @see pl.szymsoft.salon.client.adapters.out.jdbc.ClientsJdbcAdapterIntegrationTest
 */
@SuppressWarnings("JavadocReference")
@DataJdbcTest
@Transactional(propagation = NOT_SUPPORTED)
@ContextConfiguration(classes = {
        AppointmentsJdbcAdapterSpringConfiguration.class,
        ClientsJdbcAdapterSpringConfiguration.class
})
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
class AppointmentsJdbcAdapterIntegrationTest extends AppointmentsSpec {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.2-alpine");

    @RegisterExtension
    private static final JdbcTableCleanerExtension db = JdbcTableCleanerExtension.builder()
            .url(container::getJdbcUrl)
            .username(container::getUsername)
            .password(container::getPassword)
            .table(CLIENT_TABLE) // and cascade APPOINTMENT_TABLE
            .build();

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    private final Client givenClient = randomClient().toClient();

    @Autowired
    private Appointments appointments;

    @Autowired
    private Clients clients;

    @Override
    protected Appointments adapter() {
        return appointments;
    }

    @Override
    protected Id<Client> givenClientId() {
        return givenClient.getId();
    }

    @BeforeEach
    private void saveClient() {
        clients.save(givenClient);
    }
}