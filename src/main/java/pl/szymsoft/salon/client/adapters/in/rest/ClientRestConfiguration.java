package pl.szymsoft.salon.client.adapters.in.rest;

import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.rest.RestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Import(RestConfiguration.class)
public class ClientRestConfiguration {

    @Bean
    ClientRestController clientRestController(Clients clients) {
        return new ClientRestController(clients);
    }
}
