package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.Nullable;
import pl.szymsoft.salon.utils.Objects;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.Objects.requireNonNullElseGet;

public final class RandomAppointment {

    private final Appointment appointment;

    public static RandomAppointmentBuilder randomAppointment() {
        return new RandomAppointmentBuilder();
    }

    @SuppressWarnings({"PublicMethodNotExposedInInterface", "PublicInnerClass"})
    public static class RandomAppointmentBuilder {
        public Appointment toAppointment() {
            return build().appointment;
        }
    }

    @Builder
    private RandomAppointment(
            @Nullable Id<Appointment> id,
            @Nullable Id<Client> clientId,
            @Nullable LocalDateTime startTime,
            @Nullable LocalDateTime endTime,
            @Singular
            @Nullable List<Service> services
    ) {
        var ensuredStartTime = requireNonNullElseGet(startTime, LocalDateTime::now);
        appointment = Appointment.builder()
                .id(id)
                .clientId(requireNonNullElseGet(clientId, Id::random))
                .startTime(ensuredStartTime)
                .endTime(requireNonNullElseGet(endTime, () -> ensuredStartTime.plus(2L, HOURS)))
                .services(Objects.requireElseGet(services, CollectionUtils::isNotEmpty, RandomService::list))
                .build();
    }
}
