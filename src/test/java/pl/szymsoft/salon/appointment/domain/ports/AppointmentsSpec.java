package pl.szymsoft.salon.appointment.domain.ports;

import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.appointment.domain.Purchase;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static pl.szymsoft.salon.appointment.domain.RandomAppointment.randomAppointment;
import static pl.szymsoft.salon.appointment.domain.RandomPurchase.randomPurchase;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AppointmentsSpec {

    protected abstract Appointments adapter();

    protected abstract Id<Client> givenClientId();

    @Nested
    class For_a_given_client_id {

        @Test
        void an_adapter_should_not_return_any_appointments_by_that_client_id() {

            // when
            var appointments = adapter().findByClientId(givenClientId());

            // then
            assertThat(appointments).isEmpty();
        }

        @Nested
        class and_for_a_given_appointment_related_to_that_client_id {

            private final Appointment givenAppointment = randomAppointment()
                    .clientId(givenClientId())
                    .toAppointment();

            @Test
            void the_adapter_should_not_return_the_appointment_by_that_appointment_id() {

                // when
                var optionalAppointment = adapter().findById(givenAppointment.getId());

                // then
                assertThat(optionalAppointment).isEmpty();
            }

            @Nested
            class WHEN_that_appointment_was_saved {

                private Appointment thatAppointment;

                @BeforeEach
                void saveGivenAppointment() {
                    thatAppointment = adapter().save(givenAppointment);
                }

                @Test
                void that_appointment_should_have_same_properties() {
                    assertThatHaveSamePropertiesExceptVersion(thatAppointment, givenAppointment);
                }

                @Test
                void the_adapter_should_return_that_appointment_by_the_given_client_id() {

                    // when
                    var appointments = adapter().findByClientId(givenClientId());

                    // then
                    assertThat(appointments)
                            .containsExactly(thatAppointment);
                }

                @Test
                void the_adapter_should_return_that_appointment_by_that_appointment_id() {

                    // when
                    var optionalAppointment = adapter().findById(givenAppointment.getId());

                    // then
                    assertThat(optionalAppointment)
                            .hasValue(thatAppointment);
                }

                @Nested
                class AND_for_given_second_appointment_related_to_the_given_client_id {

                    private final Appointment secondAppointment = randomAppointment()
                            .clientId(givenClientId())
                            .toAppointment();

                    @Nested
                    class when_that_appointment_was_saved {

                        private Appointment secondSavedAppointment;

                        @BeforeEach
                        void saveSecondAppointment() {
                            secondSavedAppointment = adapter().save(secondAppointment);
                        }

                        @Test
                        void that_appointment_should_have_same_properties() {
                            assertThatHaveSamePropertiesExceptVersion(secondSavedAppointment, secondAppointment);
                        }

                        @Test
                        void the_adapter_should_return_both_saved_appointments_for_the_given_client_id() {

                            // when
                            var appointments = adapter().findByClientId(givenClientId());

                            // then
                            assertThat(appointments)
                                    .containsExactly(thatAppointment, secondSavedAppointment);
                        }

                        @Test
                        void the_adapter_should_return_that_appointment_by_that_appointment_id() {

                            // when
                            var optionalAppointment = adapter().findById(secondAppointment.getId());

                            // then
                            assertThat(optionalAppointment)
                                    .contains(secondSavedAppointment);
                        }
                    }
                }

                @Nested
                class THEN_when_a_given_purchase {

                    private final Purchase givenPurchase = randomPurchase().toPurchase();

                    @Nested
                    class is_added_to_that_appointment_and_saved {

                        private Appointment appointmentWithPurchase;

                        @BeforeEach
                        void when() {
                            appointmentWithPurchase = thatAppointment.add(givenPurchase);
                            adapter().save(appointmentWithPurchase);
                        }

                        @Test
                        void THEN_the_adapter_should_return_an_updated_appointment() {

                            // when
                            var updatedAppointment = adapter().findById(thatAppointment.getId());

                            // then
                            assertThat(updatedAppointment)
                                    .hasValueSatisfying(appointment ->
                                            assertThatHaveSamePropertiesExceptVersion(appointment, appointmentWithPurchase));
                        }
                    }
                }
            }
        }
    }

    private static void assertThatHaveSamePropertiesExceptVersion(Appointment actual, Appointment expected) {
        assertThat(actual)
                .as("id does match").returns(expected.getId(), Appointment::getId)
                .as("clientId does match").returns(expected.getClientId(), Appointment::getClientId)
                .as("startTime does match").returns(expected.getStartTime(), Appointment::getStartTime)
                .as("endTime does match").returns(expected.getEndTime(), Appointment::getEndTime)
                .as("services does match").returns(expected.getServices(), Appointment::getServices)
                .as("purchases does match").returns(expected.getPurchases(), Appointment::getPurchases)
                .as("version does not match").doesNotReturn(expected.getVersion(), Appointment::getVersion);
    }
}