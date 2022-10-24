package pl.szymsoft.salon.client.domain.ports;

import pl.szymsoft.salon.client.domain.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static pl.szymsoft.salon.client.domain.RandomClient.randomClient;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ClientsSpec {

    protected abstract Clients adapter();

    @Test
    void does_not_return_any_clients() {

        // when
        var clients = adapter().findAll();

        // then
        Assertions.assertThat(clients)
                .isEmpty();
    }

    @Nested
    class for_a_given_client {

        private final Client givenClient = randomClient().toClient();

        @Nested
        class after_saving_given_client {

            private Client savedClient;

            @BeforeEach
            void given() {
                savedClient = adapter().save(givenClient);
            }

            @Test
            void saved_client_has_same_properties_except_a_version() {
                assertThatHaveSamePropertiesExceptVersion(savedClient, givenClient);

            }

            @Nested
            class findAll_returns_a_list {

                private List<Client> clients;

                @BeforeEach
                void when() {
                    clients = adapter().findAll();
                }

                @Test
                void containing_only_a_given_client() {

                    // then
                    assertThat(clients)
                            .hasSize(1)
                            .anySatisfy(client -> assertThatHaveSamePropertiesExceptVersion(client, givenClient));
                }
            }

            @Nested
            class for_another_client {

                private final Client anotherClient = randomClient().toClient();

                @Nested
                class after_saving_another_client {

                    @BeforeEach
                    void given() {
                        adapter().save(anotherClient);
                    }

                    @Test
                    void findAll_returns_a_list_containing_only_both_clients() {

                        // when
                        var clients = adapter().findAll();

                        // then
                        Assertions.assertThat(clients)
                                .hasSize(2)
                                .anySatisfy(client -> assertThatHaveSamePropertiesExceptVersion(client, givenClient))
                                .anySatisfy(client -> assertThatHaveSamePropertiesExceptVersion(client, anotherClient));
                    }
                }
            }
        }
    }

    // TODO: own or generated assertion?
    private static void assertThatHaveSamePropertiesExceptVersion(Client actual, Client expected) {
        assertThat(actual)
                .as("id does match").returns(expected.getId(), Client::getId)
                .as("firstName does match").returns(expected.getFirstName(), Client::getFirstName)
                .as("lastName does match").returns(expected.getLastName(), Client::getLastName)
                .as("email does match").returns(expected.getEmail(), Client::getEmail)
                .as("phone does match").returns(expected.getPhone(), Client::getPhone)
                .as("gender does match").returns(expected.getGender(), Client::getGender)
                .as("version does not match").doesNotReturn(expected.getVersion(), Client::getVersion);
    }
}
