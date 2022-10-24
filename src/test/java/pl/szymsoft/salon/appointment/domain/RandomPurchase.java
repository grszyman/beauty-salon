package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.Price;
import pl.szymsoft.salon.values.RandomEuroPrice;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.List;

import static java.util.Objects.requireNonNullElseGet;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.types.Ints.ints;

public final class RandomPurchase {

    private static final List<String> NAMES = List.of("Anti Ageing Cream", "Body Lotion", "Body Wash", "Cleanser", "Conditioner",
            "Foundation", "Hairspray", "Hand Gel", "Lip Gloss", "Mascara", "Moistureiser", "Shampoo", "Styling Gel");

    private final Purchase purchase;

    public static RandomPurchaseBuilder randomPurchase() {
        return new RandomPurchaseBuilder();
    }

    @SuppressWarnings({"PublicMethodNotExposedInInterface", "PublicInnerClass"})
    public static class RandomPurchaseBuilder {

        public Purchase toPurchase() {
            return build().purchase;
        }
    }

    @Builder
    private RandomPurchase(
            @Nullable Id<Purchase> id,
            @Nullable String name,
            @Nullable Price price,
            @Nullable Integer loyaltyPoints
    ) {
        purchase = Purchase.builder()
                .id(requireNonNullElseGet(id, Id::<Purchase>random))
                .name(requireNonNullElseGet(name, RandomPurchase::name))
                .price(requireNonNullElseGet(price, RandomEuroPrice::upTo100))
                .loyaltyPoints(requireNonNullElseGet(loyaltyPoints,
                        () -> ints().range(0, 100).get()))
                .build();
    }

    public static String name() {
        return from(NAMES).get();
    }
}
