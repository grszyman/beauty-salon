package pl.szymsoft.salon.client.adapters.out.jdbc;

import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.client.domain.ports.ClientsSpec;
import pl.szymsoft.salon.jdbc.JdbcTableCleanerExtension;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * Initially, this class contained all the test cases directly in it and was based on
 * a {@link org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest} which, by default, rolls back a transaction after each test
 * to ensure the same state of DB for each test. Unfortunately, a transaction context is not propagated to a base class, so this approach
 * doesn't work when we have a shared base class with test cases. A solution for that is to clean chosen tables before each test.
 * It is done by {@link JdbcTableCleanerExtension}.
 */
@DataJdbcTest
@Transactional(propagation = NOT_SUPPORTED)
@ContextConfiguration(classes = ClientsJdbcAdapterSpringConfiguration.class)
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
class ClientsJdbcAdapterIntegrationTest extends ClientsSpec {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.2-alpine");

    @RegisterExtension
    private static final JdbcTableCleanerExtension db = JdbcTableCleanerExtension.builder()
            .url(container::getJdbcUrl)
            .username(container::getUsername)
            .password(container::getPassword)
            .table(CLIENT_TABLE)
            .build();

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    private Clients clientsAdapter;

    @Override
    protected Clients adapter() {
        return clientsAdapter;
    }
}