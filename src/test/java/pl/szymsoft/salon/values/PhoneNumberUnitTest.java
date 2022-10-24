package pl.szymsoft.salon.values;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PhoneNumberUnitTest {

    @Nested
    class If_a_given_string_is_a_valid_phone_number {

        record PhoneNumberSpec(String givenString, String expectedString) {
        }

        private final Set<PhoneNumberSpec> specs = Set.of(
                new PhoneNumberSpec("1157663340", "115 766 3340"),
                new PhoneNumberSpec("125-266-2240", "125 266 2240"),
                new PhoneNumberSpec("117.559.8191", "117 559 8191"),
                new PhoneNumberSpec("(723) 130-5382", "723 130 5382"),
                new PhoneNumberSpec("1-580-807-6075", "1 580 807 6075")
        );

        @Test
        void PhoneNumber$of_should_return_an_instance_of_PhoneNumber() {

            specs.forEach(spec -> {
                // when
                var phoneNumber = PhoneNumber.of(spec.givenString);

                // then
                assertThat(phoneNumber).isNotNull();
            });
        }

        @Test
        void PhoneNumber$toString_should_return_a_normalised_phone_number() {

            specs.forEach(spec -> {

                // given
                var phoneNumber = PhoneNumber.of(spec.givenString);

                // when
                var asString = phoneNumber.toString();

                // then
                assertThat(asString).as("normalised representation of " + spec.givenString)
                        .isEqualTo(spec.expectedString);
            });
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1", "12", "123", "1234", "12345", "123456", "1234567", "12345678", "123456789012",
            "aaa aaa aaaaa", "a 123 123 1245"})
    void If_a_given_string_is_not_a_valid_phone_number_PhoneNumber$of_should_throw_IllegalArgumentException(String givenString) {

        // when
        var throwable = catchThrowable(() -> PhoneNumber.of(givenString));

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Two_instances_of_PhoneNumber_should_be_equal_and_have_same_hashCode_if_have_the_same_value() {

        // given
        var phone1 = PhoneNumber.of("123 123 1234");
        var phone2 = PhoneNumber.of("123 123 1234");

        // expect
        assertThat(phone1).isEqualTo(phone2);

        assertThat(phone1.hashCode())
                .isEqualTo(phone2.hashCode());
    }
}