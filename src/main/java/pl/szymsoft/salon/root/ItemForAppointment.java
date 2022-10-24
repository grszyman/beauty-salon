package pl.szymsoft.salon.root;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.values.Id;

record ItemForAppointment<T>(Id<Appointment> appointmentId, T value) {
}
