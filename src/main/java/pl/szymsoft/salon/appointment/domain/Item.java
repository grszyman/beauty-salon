package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.values.Price;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.szymsoft.salon.utils.Strings;

@Getter
@ToString
@EqualsAndHashCode
sealed class Item permits Service, Purchase {

    private final String name;
    private final Price price;
    private final LoyaltyPoints loyaltyPoints;

    Item(
            String name,
            @NonNull Price price,
            long loyaltyPoints
    ) {
        this.name = Strings.requireNonBlank(name, "name");
        this.price = price;
        this.loyaltyPoints = LoyaltyPoints.of(loyaltyPoints);
    }

    LoyaltyPoints toLoyaltyPoints() {
        return loyaltyPoints;
    }

    public long getLoyaltyPoints() {
        return loyaltyPoints.toLong();
    }
}
