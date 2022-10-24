package pl.szymsoft.salon.client.domain;

import pl.szymsoft.salon.client.domain.Client.ClientBuilder;
import pl.szymsoft.salon.values.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SuppressWarnings("ConstantConditions")
@DisplayName("A Client")
class ClientUnitTest {

    @Nested
    class created_without_an_id {

        private final Client client = RandomClient.randomClient()
                .id(null)
                .toClient();

        @Test
        void will_get_a_generated_id() {
            // then
            assertThat(client)
                    .isNotNull()
                    .satisfies(it -> assertThat(it.getId())
                            .isNotNull());
        }

        @Test
        void will_get_a_unique_id() {

            // and when
            var otherClient = RandomClient.randomClient()
                    .id(null)
                    .toClient();

            // then
            assertThat(client.getId())
                    .isNotEqualTo(otherClient.getId());
        }
    }

    @Test
    void created_with_a_given_id_will_have_that_id() {

        // given
        var givenId = Id.<Client>random();

        // when
        var client = RandomClient.randomClient()
                .id(givenId)
                .toClient();

        // then
        assertThat(client)
                .isNotNull()
                .satisfies(it -> assertThat(it.getId())
                        .isEqualTo(givenId));
    }

    private record PropertySpec(String name, Function<ClientBuilder, ClientBuilder> mutation) {
        @Override
        public String toString() {
            return name;
        }
    }

    private static List<PropertySpec> propertySpecs() {
        return List.of(
                new PropertySpec("firstName", builder -> builder.firstName(null)),
                new PropertySpec("lastName", builder -> builder.lastName(null)),
                new PropertySpec("email", builder -> builder.email(null)),
                new PropertySpec("phone", builder -> builder.phone(null)),
                new PropertySpec("gender", builder -> builder.gender(null))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("propertySpecs")
    void cannot_be_created_without(PropertySpec spec) {

        // given
        var clientBuilder = RandomClient.randomClient()
                .toClient()
                .toBuilder();

        // when
        var throwable = catchThrowable(() -> spec.mutation.apply(clientBuilder)
                .build());

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(spec.name + " is marked non-null but is null");
    }
}