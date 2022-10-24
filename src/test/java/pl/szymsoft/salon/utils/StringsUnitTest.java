package pl.szymsoft.salon.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;

class StringsUnitTest {

    @Nested
    class A_method_Strings {

        @Nested
        class requireNonBlank_should {

            @Test
            void return_a_given_text_if_it_is_not_blank() {

                // when
                var text = Strings.requireNonBlank("given", "default");

                // then
                assertThat(text).isEqualTo("given");
            }

            @Test
            void thrown_IllegalArgumentException_if_a_given_text_is_blank() {

                // when
                var thrown = catchThrowable(() ->
                        Strings.requireNonBlank(" ", "text"));

                // then
                assertThat(thrown)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("text must not be blank");
            }
        }

        @Nested
        class requireNonBlankElse_should {

            @Test
            void return_a_given_text_if_it_is_not_blank() {

                // when
                var text = Strings.requireNonBlankElse("given", "default");

                // then
                assertThat(text).isEqualTo("given");
            }

            @Test
            void return_a_default_text_if_a_given_text_is_blank() {

                // when
                var text = Strings.requireNonBlankElse(" ", "default");

                // then
                assertThat(text).isEqualTo("default");
            }
        }

        @Nested
        class requireNonBlankElseGet_should {

            @Test
            void return_a_given_text_if_it_is_not_blank() {

                // when
                var text = Strings.requireNonBlankElseGet("given", () -> "default");

                // then
                assertThat(text).isEqualTo("given");
            }

            @Test
            void return_a_default_text_if_a_given_text_is_blank() {

                // when
                var text = Strings.requireNonBlankElseGet(" ", () -> "default");

                // then
                assertThat(text).isEqualTo("default");
            }
        }
    }
}