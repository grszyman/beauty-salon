package pl.szymsoft.salon.values;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static net.andreinc.mockneat.unit.types.Longs.longs;

public enum RandomEuroPrice {
    ;

    private static final CurrencyUnit EURO = Monetary.getCurrency("EUR");

    public static Price upTo100() {
        //noinspection MagicNumber
        return Price.of(Money.of(randomLongMax(100L), EURO));
    }

    public static Price upTo(long max) {
        return Price.of(Money.of(randomLongMax(max), EURO));
    }

    private static Long randomLongMax(long max) {
        return longs()
                .rangeClosed(0L, max)
                .get();
    }
}
