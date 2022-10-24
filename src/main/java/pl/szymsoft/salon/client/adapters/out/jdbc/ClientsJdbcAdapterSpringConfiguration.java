package pl.szymsoft.salon.client.adapters.out.jdbc;

import pl.szymsoft.salon.client.domain.ports.Clients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcRepositories(
        considerNestedRepositories = true,
        basePackageClasses = ClientsJdbcAdapterSpringConfiguration.class
)
public class ClientsJdbcAdapterSpringConfiguration {

    public static final String CLIENT_TABLE = "client";

    @Bean
    Clients clientsJdbcAdapter(ClientsJdbcAdapter.ClientRowRepository repository) {
        return new ClientsJdbcAdapter(repository);
    }
}
