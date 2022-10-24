package pl.szymsoft.salon.values;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static net.andreinc.mockneat.unit.user.Emails.emails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;

class EmailAddressUnitTest {

    @Nested
    class EmailAddress$of_should {

        @Test
        void return_an_instance_of_EmailAddress_if_a_given_string_is_a_valid_email_address() {

            // given
            var givenString = emails().get();

            // when
            var email = EmailAddress.of(givenString);

            // then
            assertThat(email)
                    .isNotNull()
                    .asString().isEqualTo(givenString);
        }

        @Test
        void throw_IllegalArgumentException_if_a_given_string_is_not_a_valid_email_address() {

            // given
            var givenString = "invalid.email";

            // when
            var thrown = catchThrowable(() -> EmailAddress.of(givenString));

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("'invalid.email' is not a valid email address");
        }
    }

    @Test
    void Two_instances_of_EmailAddress_should_be_equal_and_have_same_hashCode_if_have_the_same_value() {

        // given
        var givenString = emails().get();

        var email1 = EmailAddress.of(givenString);
        var email2 = EmailAddress.of(givenString);

        // expect
        assertThat(email1).isEqualTo(email2);

        assertThat(email1.hashCode())
                .isEqualTo(email2.hashCode());
    }
}