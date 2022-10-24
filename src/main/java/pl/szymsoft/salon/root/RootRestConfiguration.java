package pl.szymsoft.salon.root;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.appointment.domain.Appointment.AppointmentBuilder;
import pl.szymsoft.salon.appointment.domain.Purchase;
import pl.szymsoft.salon.appointment.domain.Service;
import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.values.EmailAddress;
import pl.szymsoft.salon.values.FirstName;
import pl.szymsoft.salon.values.Gender;
import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.LastName;
import pl.szymsoft.salon.values.PhoneNumber;
import pl.szymsoft.salon.values.Price;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;
import static org.apache.commons.lang3.math.NumberUtils.createBigDecimal;

@Configuration
public class RootRestConfiguration {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    RootRestController rootRestController(Clients clients, Appointments appointments) {
        return new RootRestController(
                clients,
                appointments,
                clientCsvEntityReader(),
                serviceCsvEntityReader(),
                purchaseCsvEntityReader(),
                appointmentCsvEntityReader()
        );
    }

    private static CsvEntityReader<Item<Client>> clientCsvEntityReader() {
        return CsvEntityReader.<Item<Client>>builder()
                .headers(new String[]{"id", "first_name", "last_name", "email", "phone", "gender", "banned"})
                .entityExtractor(columns -> Item.<Client>builder()
                        .value(Client.builder()
                                .id(Id.of(columns[0]))
                                .firstName(FirstName.of(columns[1]))
                                .lastName(LastName.of(columns[2]))
                                .email(EmailAddress.of(columns[3]))
                                .phone(PhoneNumber.of(columns[4]))
                                .gender(Gender.valueOf(columns[5].toUpperCase(Locale.ROOT)))
                                .banned(parseBoolean(columns[6]))
                                .build())
                        .source(columns)
                        .build())
                .build();
    }

    private static CsvEntityReader<ItemWithAppointmentId<AppointmentBuilder>> appointmentCsvEntityReader() {

        return CsvEntityReader.<ItemWithAppointmentId<AppointmentBuilder>>builder()
                .headers(new String[]{"id", "client_id", "start_time", "end_time"})
                .entityExtractor(RootRestConfiguration::toAppointmentBuilderEntry)
                .build();
    }

    private static ItemWithAppointmentId<AppointmentBuilder> toAppointmentBuilderEntry(String[] values) {
        var id = Id.<Appointment>of(values[0]);
        return ItemWithAppointmentId.<AppointmentBuilder>builder()
                .appointmentId(id)
                .value(Appointment.builder()
                        .id(id)
                        .clientId(Id.of(values[1]))
                        .startTime(ZonedDateTime.parse(values[2], DATE_TIME_FORMATTER).toLocalDateTime()) // TODO: migrate to ZonedDateTime
                        .endTime(ZonedDateTime.parse(values[3], DATE_TIME_FORMATTER).toLocalDateTime()))
                .source(values)
                .build();
    }

    private static CsvEntityReader<ItemWithAppointmentId<Service>> serviceCsvEntityReader() {
        return CsvEntityReader.<ItemWithAppointmentId<Service>>builder()
                .headers(new String[]{"id", "appointment_id", "name", "price", "loyalty_points"})
                .entityExtractor(columns -> ItemWithAppointmentId.<Service>builder()
                        .appointmentId(Id.of(columns[1]))
                        .value(Service.builder()
                                .id(Id.of(columns[0]))
                                .name(columns[2])
                                .price(Price.of(Money.of(createBigDecimal(columns[3]), "EUR"))) // TODO: a little bit too complicated ;)
                                .loyaltyPoints(parseLong(columns[4]))
                                .build())
                        .source(columns)
                        .build())
                .build();
    }

    private static CsvEntityReader<ItemWithAppointmentId<Purchase>> purchaseCsvEntityReader() {
        return CsvEntityReader.<ItemWithAppointmentId<Purchase>>builder()
                .headers(new String[]{"id", "appointment_id", "name", "price", "loyalty_points"})
                .entityExtractor(columns -> ItemWithAppointmentId.<Purchase>builder()
                        .appointmentId(Id.of(columns[1]))
                        .value(Purchase.builder()
                                .id(Id.of(columns[0]))
                                .name(columns[2])
                                .price(Price.of(Money.of(createBigDecimal(columns[3]), "EUR"))) // TODO: a little bit too complicated ;)
                                .loyaltyPoints(parseLong(columns[4]))
                                .build())
                        .source(columns)
                        .build())
                .build();
    }
}
