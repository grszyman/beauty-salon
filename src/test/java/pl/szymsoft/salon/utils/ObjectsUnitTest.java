package pl.szymsoft.salon.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;

class ObjectsUnitTest {

    @Nested
    class An_Objects_method {

        @Nested
        class require_should {

            @Test
            void return_a_given_object_if_it_matches_a_predicate() {

                // when
                var object = Objects.require("text", StringUtils::isNotBlank, "object must not be blank");

                // then
                assertThat(object).isEqualTo("text");
            }

            @Test
            void throw_IllegalArgumentException_if_a_given_object_does_not_match_a_predicate() {

                // when
                var thrown = catchThrowable(() ->
                        Objects.require(" ", StringUtils::isNotBlank, "object must not be blank"));

                // then
                assertThat(thrown)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("object must not be blank");
            }
        }

        @Nested
        class requireElse_should {
            @Test
            void return_a_given_object_if_it_matches_a_predicate() {

                // when
                var object = Objects.requireElse("text", StringUtils::isNotBlank, "object must not be blank");

                // then
                assertThat(object).isEqualTo("text");
            }

            @Test
            void return_a_default_object_if_a_given_object_does_not_match_a_predicate() {

                // when
                var object = Objects.requireElse(" ", StringUtils::isNotBlank, "default");

                // then
                assertThat(object).isEqualTo("default");
            }
        }

        @Nested
        class requireElseGet_should {
            @Test
            void return_a_given_object_if_it_matches_a_predicate() {

                // when
                var object = Objects.requireElseGet("text", StringUtils::isNotBlank, () -> "default");

                // then
                assertThat(object).isEqualTo("text");
            }

            @Test
            void return_an_object_from_a_supplier_if_a_given_object_does_not_match_a_a_predicate() {

                // when
                var object = Objects.requireElseGet(" ", StringUtils::isNotBlank, () -> "default");

                // then
                assertThat(object).isEqualTo("default");
            }
        }
    }
}