package pl.szymsoft.salon.appointment.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.szymsoft.salon.utils.Objects;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@RequiredArgsConstructor(access = PRIVATE)
final class LoyaltyPoints {

    static final LoyaltyPoints ZERO = of(0L);

    private final long value;

    static LoyaltyPoints of(long value) {
        return new LoyaltyPoints(Objects.require(value, amount -> amount >= 0L, "value must not be negative"));
    }

    long toLong() {
        return value;
    }

    LoyaltyPoints add(LoyaltyPoints points) {
        return new LoyaltyPoints(value + points.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
