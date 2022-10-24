package pl.szymsoft.salon.values;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class LastNameUnitTest {

    @Nested
    class LastName$of_should {
        @Test
        void return_an_instance_of_LastName_if_a_given_string_is_not_blank() {

            // given
            var givenString = "Collins";

            // when
            var lastName = LastName.of(givenString);

            // then
            assertThat(lastName)
                    .isNotNull()
                    .asString().isEqualTo(givenString);
        }

        @Test
        void thrown_IllegalArgumentException_if_a_given_string_is_blank() {

            //given
            var givenString = " ";

            // when
            var throwable = catchThrowable(() -> LastName.of(givenString));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("value must not be blank");
        }
    }

    @Test
    void Two_instances_of_LastName_should_be_equal_and_have_same_hashCode_if_have_the_same_value() {

        // given
        var collins1 = LastName.of("Collins");
        var collins2 = LastName.of("Collins");

        // expect
        assertThat(collins1).isEqualTo(collins2);

        assertThat(collins1.hashCode())
                .isEqualTo(collins2.hashCode());
    }

}