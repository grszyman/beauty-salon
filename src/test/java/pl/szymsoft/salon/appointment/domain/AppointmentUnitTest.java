package pl.szymsoft.salon.appointment.domain;

import pl.szymsoft.salon.appointment.domain.Appointment.AppointmentBuilder;
import pl.szymsoft.salon.values.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static pl.szymsoft.salon.appointment.domain.RandomAppointment.randomAppointment;
import static pl.szymsoft.salon.appointment.domain.RandomPurchase.randomPurchase;
import static pl.szymsoft.salon.appointment.domain.RandomService.randomService;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("An Appointment")
@SuppressWarnings("ConstantConditions")
class AppointmentUnitTest {

    @Nested
    class created_without_an_id {

        private final Appointment appointment = randomAppointment()
                .id(null)
                .toAppointment();

        @Test
        void will_get_a_generated_id() {
            // then
            assertThat(appointment)
                    .isNotNull()
                    .satisfies(it -> assertThat(it.getId())
                            .isNotNull());
        }

        @Test
        void will_get_a_unique_id() {

            // and when
            var otherAppointment = randomAppointment()
                    .id(null)
                    .toAppointment();

            // then
            assertThat(appointment.getId())
                    .isNotEqualTo(otherAppointment.getId());
        }
    }

    @Test
    void created_with_a_given_id_will_have_that_id() {

        // given
        var givenId = Id.<Appointment>random();

        // when
        var appointment = randomAppointment()
                .id(givenId)
                .toAppointment();

        // then
        assertThat(appointment)
                .isNotNull()
                .satisfies(it -> assertThat(it.getId())
                        .isEqualTo(givenId));
    }

    @Nested
    class cannot_be_created {

        private record PropertySpec(String name, Function<AppointmentBuilder, AppointmentBuilder> mutation) {
            @Override
            public String toString() {
                return name;
            }
        }

        @SuppressWarnings("ConstantConditions")
        private static List<PropertySpec> propertySpecs() {
            return List.of(
                    new PropertySpec("clientId", builder -> builder.clientId(null)),
                    new PropertySpec("startTime", builder -> builder.startTime(null)),
                    new PropertySpec("endTime", builder -> builder.endTime(null)),
                    new PropertySpec("services", builder -> builder.services(null))
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("propertySpecs")
        void without(PropertySpec spec) {

            // given
            var appointmentBuilder = anAppointmentWith();

            // when
            var throwable = catchThrowable(() ->
                    spec.mutation.apply(appointmentBuilder).build());

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void without_at_least_one_service() {

            // given
            var givenServices = Collections.<Service>emptyList();

            // when
            var throwable = catchThrowable(() -> Appointment.builder()
                    .clientId(Id.random())
                    .startTime(now())
                    .endTime(now().plus(1L, HOURS))
                    .services(givenServices)
                    .build());

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("services must not be empty");
        }

        @Test
        void with_a_start_time_after_an_end_time() {

            // given
            var startTime = now();
            var endTime = startTime.minus(1L, HOURS);
            var validAppointment = randomAppointment().toAppointment();

            // when
            var throwable = catchThrowable(() -> validAppointment.toBuilder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .build());

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("startTime must be before endTime");
        }

        private static AppointmentBuilder anAppointmentWith() {
            return randomAppointment()
                    .toAppointment()
                    .toBuilder();
        }
    }

    @Test
    void can_be_created_with_a_start_time_before_an_end_time() {

        // given
        var startTime = now();
        var endTime = startTime.plus(1L, HOURS);
        var validAppointment = randomAppointment().toAppointment();

        // when
        var appointment = validAppointment.toBuilder()
                .startTime(startTime)
                .endTime(endTime)
                .build();

        // then
        assertThat(appointment.getStartTime())
                .isEqualTo(startTime);
        assertThat(appointment.getEndTime())
                .isEqualTo(endTime);
    }

    @Nested
    class created_with_a_service {

        private final Service givenService = randomService().toService();

        private Appointment givenAppointment = randomAppointment()
                .service(givenService)
                .toAppointment();

        @Test
        void should_have_a_sum_of_loyalty_points_equal_to_the_loyalty_points_of_that_service() {

            // when
            var loyaltyPoints = givenAppointment.getLoyaltyPoints();

            // then
            assertThat(loyaltyPoints)
                    .isEqualTo(givenService.getLoyaltyPoints());
        }

        @Nested
        class when_add_another_service {

            // given
            private final Service anotherService = randomService().toService();

            @BeforeEach
            void when() {
                givenAppointment = givenAppointment.add(anotherService);
            }

            @Test
            void should_have_a_sum_of_loyalty_points_for_both_services() {
                // then
                var expectedPoints = givenService.getLoyaltyPoints() + anotherService.getLoyaltyPoints();

                assertThat(givenAppointment)
                        .returns(expectedPoints, Appointment::getLoyaltyPoints)
                        .extracting(Appointment::getServices)
                        .asList()
                        .hasSize(2)
                        .containsExactly(givenService, anotherService);
            }
        }

        @Nested
        class when_add_a_purchase {

            private final Purchase givenPurchase = randomPurchase().toPurchase();

            @BeforeEach
            void when() {
                givenAppointment = givenAppointment.add(givenPurchase);
            }

            @Test
            void should_have_a_sum_of_loyalty_points_for_both_items() {

                // then
                var expectedPoints = givenService.getLoyaltyPoints() + givenPurchase.getLoyaltyPoints();

                assertThat(givenAppointment)
                        .returns(expectedPoints, Appointment::getLoyaltyPoints);
            }

            @Test
            void should_contain_both_items() {

                // then
                assertThat(givenAppointment.getServices())
                        .containsExactly(givenService);
                assertThat((givenAppointment.getPurchases()))
                        .containsExactly(givenPurchase);
            }
        }

        @Nested
        class and_a_purchase {

            private final Purchase givenPurchase = randomPurchase().toPurchase();

            @BeforeEach
            void when() {
                givenAppointment = givenAppointment.toBuilder()
                        .purchase(givenPurchase)
                        .build();
            }

            @Test
            void should_contain_both_items() {

                // then
                assertThat(givenAppointment.getServices())
                        .containsExactly(givenService);

                assertThat(givenAppointment.getPurchases())
                        .containsExactly(givenPurchase);
            }

            @Test
            void should_have_a_sum_of_loyalty_points_for_both_items() {

                // then
                var expectedPoints = givenService.getLoyaltyPoints() + givenPurchase.getLoyaltyPoints();

                assertThat(givenAppointment.getLoyaltyPoints())
                        .isEqualTo(expectedPoints);
            }
        }
    }
}