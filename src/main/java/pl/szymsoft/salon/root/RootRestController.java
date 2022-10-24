package pl.szymsoft.salon.root;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.appointment.domain.Appointment.AppointmentBuilder;
import pl.szymsoft.salon.appointment.domain.Purchase;
import pl.szymsoft.salon.appointment.domain.Service;
import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.root.FileProcessingResult.LineError;
import pl.szymsoft.salon.values.Id;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
class RootRestController {

    private static final String APPOINTMENT_NOT_FOUND = "A matching appointment has not been found.";

    @NonNull
    private final Clients clients;
    @NonNull
    private final Appointments appointments;

    @NonNull
    private final CsvEntityReader<Item<Client>> clientCsvReader;
    @NonNull
    private final CsvEntityReader<ItemWithAppointmentId<Service>> serviceCsvReader;
    @NonNull
    private final CsvEntityReader<ItemWithAppointmentId<Purchase>> purchasesReader;
    @NonNull
    private final CsvEntityReader<ItemWithAppointmentId<AppointmentBuilder>> appointmentCsvReader;

    @PostMapping
    ResponseEntity<?> uploadFiles(
            @RequestParam("clients") MultipartFile clientsFile,
            @RequestParam("appointments") MultipartFile appointmentsFile,
            @RequestParam("services") MultipartFile servicesFile,
            @RequestParam("purchases") MultipartFile purchasesFile
    ) {
        var clientsResult = save(clientCsvReader.readFrom("clients", clientsFile));

        var appointmentsBuildersAndResult = appointmentCsvReader.readFrom("appointments", appointmentsFile);
        var appointmentBuildersById = appointmentsBuildersAndResult.getEntities()
                .stream()
                .collect(toMap(ItemWithAppointmentId::appointmentId, identity()));

        var servicesResult = addServicesToAppointments(
                serviceCsvReader.readFrom("services", servicesFile),
                appointmentBuildersById);

        var purchasesResult = addPurchasesToAppointments(
                purchasesReader.readFrom("purchases", purchasesFile),
                appointmentBuildersById);

        var appointmentsResult = save(appointmentsBuildersAndResult.getResult(), appointmentBuildersById.values());

        return ResponseEntity.ok()
                .body(List.of(clientsResult, appointmentsResult, servicesResult, purchasesResult));
    }

    private FileProcessingResult save(
            FileProcessingResult processingResult,
            Iterable<ItemWithAppointmentId<AppointmentBuilder>> appointmentsBuilders
    ) {
        var resultBuilder = processingResult.toBuilder();
        appointmentsBuilders
                .forEach(item -> Try.ofSupplier(() -> item.value().build())
                        .onFailure(ex -> resultBuilder.lineError(new LineError(ex.getMessage(), item.source())))
                        .map(appointment -> Try.ofCallable(() -> appointments.save(appointment))
                                .onFailure(ex -> resultBuilder.lineError(new LineError(ex.getMessage(), item.source())))));

        return resultBuilder.build();
    }

    private static FileProcessingResult addPurchasesToAppointments(
            EntitiesAndProcessingResult<ItemWithAppointmentId<Purchase>> purchasesAndResults,
            Map<Id<Appointment>, ItemWithAppointmentId<AppointmentBuilder>> appointmentBuildersById
    ) {
        var resultBuilder = purchasesAndResults.getResult().toBuilder();
        purchasesAndResults.getEntities()
                .forEach(purchaseItem -> Optional.ofNullable(appointmentBuildersById.get(purchaseItem.appointmentId()))
                        .ifPresentOrElse(appointmentBuilderItem -> appointmentBuilderItem.value().purchase(purchaseItem.value()),
                                () -> resultBuilder.lineError(new LineError(APPOINTMENT_NOT_FOUND, purchaseItem.source()))));
        return resultBuilder.build();
    }

    private static FileProcessingResult addServicesToAppointments(
            EntitiesAndProcessingResult<ItemWithAppointmentId<Service>> servicesResult,
            Map<Id<Appointment>, ItemWithAppointmentId<AppointmentBuilder>> appointmentBuildersById
    ) {
        var resultBuilder = servicesResult.getResult().toBuilder();
        servicesResult.getEntities()
                .forEach(serviceItem -> Optional.ofNullable(appointmentBuildersById.get(serviceItem.appointmentId()))
                        .ifPresentOrElse(appointmentBuilderItem -> appointmentBuilderItem.value().service(serviceItem.value()),
                                () -> resultBuilder.lineError(new LineError(APPOINTMENT_NOT_FOUND, serviceItem.source()))));
        return resultBuilder.build();
    }

    private FileProcessingResult save(EntitiesAndProcessingResult<Item<Client>> clientsAndResult) {

        var resultBuilder = clientsAndResult.getResult().toBuilder();

        clientsAndResult.getEntities()
                .forEach(item -> Try.ofCallable(() -> clients.save(item.value()))
                        .onFailure(ex -> resultBuilder.lineError(new LineError(ex.getMessage(), item.source()))));

        return resultBuilder.build();
    }
}
