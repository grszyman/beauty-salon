package pl.szymsoft.salon.root;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.values.Id;
import lombok.Builder;

@Builder
record ItemWithAppointmentId<T>(
        Id<Appointment> appointmentId,
        T value,
        String[] source) {
}
