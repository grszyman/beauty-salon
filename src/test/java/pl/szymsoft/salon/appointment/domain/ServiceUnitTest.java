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

import static pl.szymsoft.salon.appointment.domain.RandomService.randomService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("A Service")
@SuppressWarnings({"ConstantConditions", "AutoBoxing"})
class ServiceUnitTest {

    private static final int LOYALTY_POINTS = 10;

    @Nested
    class created_without_an_id {

        private final Service service = new Service(RandomService.name(), RandomEuroPrice.upTo100(), LOYALTY_POINTS);

        @Test
        void will_get_a_generated_id() {
            // then
            assertThat(service)
                    .isNotNull()
                    .satisfies(it -> assertThat(it.getId())
                            .isNotNull());
        }

        @Test
        void will_get_a_unique_id() {

            // and when
            var otherService = new Service(RandomService.name(), RandomEuroPrice.upTo100(), LOYALTY_POINTS);

            // then
            assertThat(service.getId())
                    .isNotEqualTo(otherService.getId());
        }
    }

    @Test
    void created_with_a_given_id_will_have_that_id() {

        // given
        var givenId = Id.<Service>random();

        // when
        var service = randomService()
                .id(givenId)
                .toService();

        // then
        assertThat(service)
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
                    new Service(givenName, RandomEuroPrice.upTo100(), LOYALTY_POINTS));

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
                    new Service(RandomService.name(), givenPrice, LOYALTY_POINTS));

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
                    new Service(RandomService.name(), RandomEuroPrice.upTo100(), givenPoints));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("value must not be negative");
        }
    }
}