package pl.szymsoft.salon.appointment.adapters.out.jdbc;

import pl.szymsoft.salon.appointment.domain.Service;
import pl.szymsoft.salon.values.Price;
import org.javamoney.moneta.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("service")
record ServiceRow(
        @Id UUID id,
        String name,
        BigDecimal price,
        long loyaltyPoints
) {
    static ServiceRow from(Service service) {
        return new ServiceRow(
                service.getId()
                        .toUUID(),
                service.getName(),
                service.getPrice()
                        .toMonetaryAmount()
                        .getNumber()
                        .numberValue(BigDecimal.class),
                service.getLoyaltyPoints()
        );
    }

    Service toService() {
        return Service.builder()
                .id(pl.szymsoft.salon.values.Id.of(id))
                .name(name)
                .price(Price.of(Money.of(price, "EUR"))) // TODO: hardcoded
                .loyaltyPoints(loyaltyPoints)
                .build();
    }
}
