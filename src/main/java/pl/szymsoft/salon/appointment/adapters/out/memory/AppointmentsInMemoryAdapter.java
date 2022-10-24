package pl.szymsoft.salon.appointment.adapters.out.memory;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;
import static java.util.stream.Stream.concat;

public class AppointmentsInMemoryAdapter implements Appointments {

    private static final List<Appointment> EMPTY_LIST = emptyList();

    private final Map<Id<Appointment>, Appointment> appointmentById = new ConcurrentHashMap<>();
    private final Map<Id<Client>, List<Appointment>> appointmentsByClientId = new ConcurrentHashMap<>();

    @Override
    public Appointment save(Appointment appointment) {
        var appointmentToSave = incrementVersion(appointment);
        appointmentById.put(appointment.getId(), appointmentToSave);
        appointmentsByClientId.merge(appointmentToSave.getClientId(), List.of(appointmentToSave),
                (a1, a2) -> concat(a1.stream(), a2.stream()).toList());
        return appointmentToSave;
    }

    @Override
    public Optional<Appointment> findById(Id<Appointment> id) {
        return Optional.ofNullable(appointmentById.get(id));
    }

    @Override
    public List<Appointment> findByClientId(Id<Client> clientId) {
        return appointmentsByClientId.getOrDefault(clientId, EMPTY_LIST);
    }

    private static Appointment incrementVersion(Appointment appointment) {
        //noinspection AutoBoxing,AutoUnboxing
        var newVersion = appointment.getVersion()
                .map(version -> version + 1L)
                .orElse(0L);

        return appointment.toBuilder()
                .version(newVersion)
                .build();
    }
}
