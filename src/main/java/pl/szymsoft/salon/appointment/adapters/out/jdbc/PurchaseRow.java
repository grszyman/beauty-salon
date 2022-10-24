package pl.szymsoft.salon.appointment.adapters.out.jdbc;

import pl.szymsoft.salon.appointment.domain.Purchase;
import pl.szymsoft.salon.values.Price;
import org.javamoney.moneta.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("purchase")
record PurchaseRow(
        @Id UUID id,
        String name,
        BigDecimal price,
        long loyaltyPoints
) {
    static PurchaseRow from(Purchase purchase) {
        return new PurchaseRow(
                purchase.getId()
                        .toUUID(),
                purchase.getName(),
                purchase.getPrice()
                        .toMonetaryAmount()
                        .getNumber()
                        .numberValue(BigDecimal.class),
                purchase.getLoyaltyPoints()
        );
    }

    Purchase toPurchase() {
        return Purchase.builder()
                .id(pl.szymsoft.salon.values.Id.of(id))
                .name(name)
                .price(Price.of(Money.of(price, "EUR"))) // TODO: hardcoded
                .loyaltyPoints(loyaltyPoints)
                .build();
    }
}
