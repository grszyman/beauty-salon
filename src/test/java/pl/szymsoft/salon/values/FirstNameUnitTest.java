package pl.szymsoft.salon.values;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class FirstNameUnitTest {

    @Nested
    class FirstName$of_should {

        @Test
        void return_an_instance_of_FirstName_if_a_given_string_is_not_blank() {

            // given
            var givenString = "Tom";

            // when
            var firstName = FirstName.of(givenString);

            // then
            assertThat(firstName)
                    .isNotNull()
                    .asString().isEqualTo(givenString);
        }

        @Test
        void thrown_IllegalArgumentException_if_a_given_string_is_blank() {

            // given
            var givenString = " ";

            // when
            var throwable = catchThrowable(() -> FirstName.of(givenString));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("value must not be blank");
        }
    }

    @Test
    void Two_instances_of_FirstName_should_be_equal_and_have_same_hashCode_if_have_the_same_value() {

        // given
        var jake1 = FirstName.of("Jake");
        var jake2 = FirstName.of("Jake");

        // expect
        assertThat(jake1).isEqualTo(jake2);

        assertThat(jake1.hashCode())
                .isEqualTo(jake2.hashCode());
    }

}