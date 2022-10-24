package pl.szymsoft.salon.appointment.adapters.out.jdbc;

import pl.szymsoft.salon.appointment.domain.Appointment;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static pl.szymsoft.salon.appointment.adapters.out.jdbc.AppointmentsJdbcAdapterSpringConfiguration.APPOINTMENT_TABLE;

@Table(APPOINTMENT_TABLE)
record AppointmentRow(
        @Id UUID id,
        UUID clientId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        @MappedCollection(idColumn = "appointment_id", keyColumn = "id")
        List<ServiceRow> services,
        @MappedCollection(idColumn = "appointment_id", keyColumn = "id")
        List<PurchaseRow> purchases,
        @Nullable @Version Long version
) {
    static AppointmentRow from(Appointment appointment) {
        return new AppointmentRow(
                appointment.getId().toUUID(),
                appointment.getClientId().toUUID(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getServices()
                        .stream()
                        .map(ServiceRow::from)
                        .toList(),
                appointment.getPurchases()
                        .stream()
                        .map(PurchaseRow::from)
                        .toList(),
                appointment.getVersion()
                        .orElse(null)
        );
    }

    Appointment toAppointment() {
        return Appointment.builder()
                .id(pl.szymsoft.salon.values.Id.of(id))
                .clientId(pl.szymsoft.salon.values.Id.of(clientId))
                .startTime(startTime)
                .endTime(endTime)
                .services(services.stream()
                        .map(ServiceRow::toService)
                        .toList())
                .purchases(purchases.stream()
                        .map(PurchaseRow::toPurchase)
                        .toList())
                .version(version)
                .build();
    }
}
