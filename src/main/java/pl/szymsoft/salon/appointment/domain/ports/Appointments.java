package pl.szymsoft.salon.appointment.domain.ports;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;

import java.util.List;
import java.util.Optional;

public interface Appointments {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Id<Appointment> id);

    List<Appointment> findByClientId(Id<Client> clientId);
}
