package pl.szymsoft.salon.client.domain.ports;

import pl.szymsoft.salon.client.domain.Client;

import java.util.List;

// Port
public interface Clients {

    Client save(Client client);

    List<Client> findAll();
}
