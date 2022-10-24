package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.Price;
import pl.szymsoft.salon.values.RandomEuroPrice;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.List;

import static java.util.Objects.requireNonNullElseGet;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.types.Longs.longs;

final class RandomService {

    private static final List<String> NAMES = List.of("Blowdry", "Cut & Style", "Eyebrow Tint", "Eyebrow Wax",
            "Full Head Colour", "Leg Wax", "Luxury Tri-Active Facial", "Manicure", "Scalp Massage", "Peel", "Skin Peel");

    private final Service service;

    static List<Service> list() {
        return List.of(new RandomServiceBuilder().toService());
    }

    static RandomServiceBuilder randomService() {
        return new RandomServiceBuilder();
    }

    @SuppressWarnings({"PublicMethodNotExposedInInterface", "PublicInnerClass"})
    public static class RandomServiceBuilder {

        Service toService() {
            return build().service;
        }
    }

    @Builder
    private RandomService(
            @Nullable Id<Service> id,
            @Nullable String name,
            @Nullable Price price,
            @Nullable Long loyaltyPoints
    ) {
        service = Service.builder()
                .id(requireNonNullElseGet(id, Id::<Service>random))
                .name(requireNonNullElseGet(name, RandomService::name))
                .price(requireNonNullElseGet(price, RandomEuroPrice::upTo100))
                .loyaltyPoints(requireNonNullElseGet(loyaltyPoints,
                        () -> longs().rangeClosed(0L, 100L).get()))
                .build();
    }

    public static String name() {
        return from(NAMES).get();
    }
}
