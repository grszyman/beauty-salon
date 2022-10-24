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
public class Service extends Item {

    Id<Service> id;

    @Builder
    private Service(
            @NonNull Id<Service> id,
            String name,
            Price price,
            long loyaltyPoints
    ) {
        super(name, price, loyaltyPoints);
        this.id = id;
    }

    public Service(String name, Price price, long loyaltyPoints) {
        this(Id.random(), name, price, loyaltyPoints);
    }
}
