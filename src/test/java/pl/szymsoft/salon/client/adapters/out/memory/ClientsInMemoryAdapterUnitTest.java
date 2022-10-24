package pl.szymsoft.salon.client.adapters.out.memory;

import pl.szymsoft.salon.client.domain.ports.ClientsSpec;
import pl.szymsoft.salon.client.domain.ports.Clients;

class ClientsInMemoryAdapterUnitTest extends ClientsSpec {

    private final Clients adapter = new ClientsInMemoryAdapter();

    @Override
    protected Clients adapter() {
        return adapter;
    }
}