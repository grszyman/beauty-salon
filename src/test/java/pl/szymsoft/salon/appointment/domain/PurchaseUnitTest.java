package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.values.Id;
import pl.szymsoft.salon.values.Price;
import pl.szymsoft.salon.values.RandomEuroPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("A Purchase")
@SuppressWarnings({"ConstantConditions", "AutoBoxing"})
class PurchaseUnitTest {

    private static final int LOYALTY_POINTS = 10;

    @Nested
    class created_without_an_id {

        private final Purchase purchase = new Purchase(
                RandomPurchase.name(),
                RandomEuroPrice.upTo100(),
                LOYALTY_POINTS
        );

        @Test
        void will_get_a_generated_id() {
            // then
            assertThat(purchase)
                    .isNotNull()
                    .satisfies(it -> assertThat(it.getId())
                            .isNotNull());
        }

        @Test
        void will_get_a_unique_id() {

            // and when
            var otherPurchase = new Purchase(
                    RandomPurchase.name(),
                    RandomEuroPrice.upTo100(),
                    LOYALTY_POINTS
            );

            // then
            assertThat(purchase.getId())
                    .isNotEqualTo(otherPurchase.getId());
        }
    }

    @Test
    void created_with_a_given_id_will_have_that_id() {

        // given
        var givenId = Id.<Purchase>random();

        // when
        var purchase = RandomPurchase.randomPurchase()
                .id(givenId)
                .toPurchase();

        // then
        assertThat(purchase)
                .isNotNull()
                .satisfies(it -> assertThat(it.getId())
                        .isEqualTo(givenId));
    }

    @Nested
    class cannot_be_created {

        @ParameterizedTest(name = "''{0}''")
        @NullAndEmptySource
        @ValueSource(strings = " ")
        void with_invalid_name(String givenName) {

            // when
            var throwable = catchThrowable(() ->
                    new Purchase(givenName, RandomEuroPrice.upTo100(), LOYALTY_POINTS));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("name must not be blank");
        }

        @Test
        void without_a_price() {

            // given
            Price givenPrice = null;

            // when
            var throwable = catchThrowable(() ->
                    new Purchase(RandomPurchase.name(), givenPrice, LOYALTY_POINTS));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("price is marked non-null but is null");
        }

        @Test
        void with_negative_loyalty_points() {

            // given
            var givenPoints = -10;

            // when
            var throwable = catchThrowable(() ->
                    new Purchase(RandomPurchase.name(), RandomEuroPrice.upTo100(), givenPoints));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("value must not be negative");
        }
    }
}