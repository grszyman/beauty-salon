package pl.szymsoft.salon.values;

import lombok.EqualsAndHashCode;
import pl.szymsoft.salon.utils.Objects;

import javax.money.MonetaryAmount;

@EqualsAndHashCode
public final class Price {

    private final MonetaryAmount value;

    private Price(MonetaryAmount value) {
        this.value = Objects.require(value, MonetaryAmount::isPositiveOrZero, "value must not be negative");
    }

    public static Price of(MonetaryAmount value) {
        return new Price(value);
    }

    public MonetaryAmount toMonetaryAmount() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
