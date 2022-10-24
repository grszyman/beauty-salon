package pl.szymsoft.salon.client.adapters.out.memory;

import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.values.Id;
import org.springframework.lang.Nullable;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: examine with the same spec as ClientsJdbcAdapter
// TODO: due to the lack of time I have to skip tests for now :(
public class ClientsInMemoryAdapter implements Clients {

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<Id<Client>, Client> clientsById = new ConcurrentHashMap<>();

    @Override
    public Client save(Client client) {
        var clientToSave = incrementVersion(client);
        var previousClient = clientsById.put(client.getId(), clientToSave);
        assertSameVersions(client, previousClient);
        return clientToSave;
    }

    @Override
    public List<Client> findAll() {
        return clientsById.values()
                .stream()
                .toList();
    }

    private static Client incrementVersion(Client client) {
        //noinspection AutoBoxing,AutoUnboxing
        var newVersion = client.getVersion()
                .map(version -> version + 1L)
                .orElse(0L);

        return client.toBuilder()
                .version(newVersion)
                .build();
    }

    private static void assertSameVersions(Client client, @Nullable Client previousClient) {
        if (previousClient != null) {
            if (!client.getVersion()
                    .equals(previousClient.getVersion())) {
                throw new ConcurrentModificationException("version does not match");
            }
        }
    }
}
