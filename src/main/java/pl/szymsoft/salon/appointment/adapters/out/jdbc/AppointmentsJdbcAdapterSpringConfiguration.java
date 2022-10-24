package pl.szymsoft.salon.appointment.adapters.out.jdbc;

import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcRepositories(
        considerNestedRepositories = true,
        basePackageClasses = AppointmentsJdbcAdapterSpringConfiguration.class
)
public class AppointmentsJdbcAdapterSpringConfiguration {

    static final String APPOINTMENT_TABLE = "appointment";

    @Bean
    Appointments appointmentsJdbcAdapter(AppointmentsJdbcAdapter.AppointmentRowRepository repository) {
        return new AppointmentsJdbcAdapter(repository);
    }
}
