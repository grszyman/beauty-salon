package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.Nullable;
import pl.szymsoft.salon.utils.Ensure;
import pl.szymsoft.salon.utils.Objects;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static pl.szymsoft.salon.utils.Ensure.ensureThat;
import static java.util.Objects.requireNonNullElseGet;
import static java.util.stream.Stream.concat;

// Aggregate Root
@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
public final class Appointment {

    private final Id<Appointment> id;
    private final Id<Client> clientId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<Service> services;
    private final List<Purchase> purchases;
    private final LoyaltyPoints loyaltyPoints;
    @Nullable
    private final Long version;

    @Builder(toBuilder = true)
    private Appointment(
            @Nullable Id<Appointment> id,
            @NonNull Id<Client> clientId,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime endTime,
            @Singular Collection<Service> services,
            @Singular Collection<Purchase> purchases,
            @Nullable Long version
    ) {
        this.id = requireNonNullElseGet(id, Id::random);
        this.clientId = clientId;

        Ensure.ensureThat(startTime.isBefore(endTime), "startTime must be before endTime");
        this.startTime = startTime;
        this.endTime = endTime;

        this.services = List.copyOf(Objects.require(services, CollectionUtils::isNotEmpty, "services must not be empty"));
        this.purchases = List.copyOf(purchases);

        loyaltyPoints = sumOfLoyaltyPoints(services, purchases);

        this.version = version;
    }

    public long getLoyaltyPoints() {
        return loyaltyPoints.toLong();
    }

    public Appointment add(Service service) {
        return toBuilder()
                .service(service)
                .build();
    }

    public Appointment add(Purchase purchase) {
        return toBuilder()
                .purchase(purchase)
                .build();
    }

    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }

    private static LoyaltyPoints sumOfLoyaltyPoints(Collection<Service> services, Collection<Purchase> purchases) {
        return concat(services.stream(), purchases.stream())
                .map(Item::toLoyaltyPoints)
                .reduce(LoyaltyPoints::add)
                .orElse(LoyaltyPoints.ZERO);
    }
}
