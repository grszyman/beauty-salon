package pl.szymsoft.salon.values;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("A Price")
class PriceUnitTest {

    @Test
    void cannot_be_created_with_an_a_negative_value() {

        // given
        var givenMoney = Money.of(-1L, "EUR");

        // when
        var throwable = catchThrowable(() -> Price.of(givenMoney));

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value must not be negative");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1L})
    void can_be_created_with_a_valid_value(long givenValue) {

        // given
        var givenMoney = Money.of(givenValue, "EUR");

        // when
        var price = Price.of(givenMoney);

        // then
        assertThat(price)
                .isNotNull()
                .returns(givenMoney, Price::toMonetaryAmount);
    }
}