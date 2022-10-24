package pl.szymsoft.salon.appointment.adapters.out.jdbc;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class AppointmentsJdbcAdapter implements Appointments {

    @NonNull
    private final AppointmentRowRepository repository;

    @Override
    public Appointment save(Appointment appointment) {
        return repository.save(AppointmentRow.from(appointment))
                .toAppointment();
    }

    @Override
    public Optional<Appointment> findById(Id<Appointment> id) {
        return repository.findById(id.toUUID())
                .map(AppointmentRow::toAppointment);
    }

    @Override
    public List<Appointment> findByClientId(Id<Client> clientId) {
        return repository.findByClientId(clientId.toUUID())
                .stream()
                .map(AppointmentRow::toAppointment)
                .toList();
    }

    interface AppointmentRowRepository extends Repository<AppointmentRow, UUID> {

        AppointmentRow save(AppointmentRow row);

        Optional<AppointmentRow> findById(UUID id);

        List<AppointmentRow> findByClientId(UUID clientId);
    }
}
