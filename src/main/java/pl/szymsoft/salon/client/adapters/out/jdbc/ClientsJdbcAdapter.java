package pl.szymsoft.salon.client.adapters.out.jdbc;

import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class ClientsJdbcAdapter implements Clients {

    @NonNull
    private final ClientRowRepository repository;

    @Override
    public Client save(Client client) {
        return repository
                .save(ClientRow.from(client))
                .toClient();
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll()
                .stream()
                .map(ClientRow::toClient)
                .toList();
    }


    interface ClientRowRepository extends Repository<ClientRow, UUID> {

        ClientRow save(ClientRow row);

        List<ClientRow> findAll();
    }
}
