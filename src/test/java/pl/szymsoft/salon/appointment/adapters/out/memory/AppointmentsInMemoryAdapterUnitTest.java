package pl.szymsoft.salon.appointment.adapters.out.memory;

import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.appointment.domain.ports.AppointmentsSpec;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;

class AppointmentsInMemoryAdapterUnitTest extends AppointmentsSpec {

    private final Appointments adapter = new AppointmentsInMemoryAdapter();
    private final Id<Client> givenClientId = Id.random();

    @Override
    protected Appointments adapter() {
        return adapter;
    }

    @Override
    protected Id<Client> givenClientId() {
        return givenClientId;
    }
}