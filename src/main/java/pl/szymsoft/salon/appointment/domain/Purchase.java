package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.Price;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Purchase extends Item {

    Id<Purchase> id;

    @Builder
    private Purchase(
            @NonNull Id<Purchase> id,
            String name,
            Price price,
            long loyaltyPoints
    ) {
        super(name, price, loyaltyPoints);
        this.id = id;
    }

    public Purchase(String name, Price price, long loyaltyPoints) {
        this(Id.random(), name, price, loyaltyPoints);
    }
}
