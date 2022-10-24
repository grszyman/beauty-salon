package pl.szymsoft.salon.client.adapters.in.rest;

import pl.szymsoft.salon.client.adapters.out.memory.ClientsInMemoryAdapter;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.values.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static pl.szymsoft.salon.client.adapters.in.rest.ClientRestControllerIntegrationTest.TestConfiguration;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
@WebMvcTest
@ContextConfiguration(classes = TestConfiguration.class)
// TODO: support for ETag
class ClientRestControllerIntegrationTest {

    @Configuration
    @Import(ClientRestConfiguration.class)
    static class TestConfiguration {
        @Bean
        Clients clients() {
            return new ClientsInMemoryAdapter();
        }
    }

    @Autowired
    private MockMvc mvc;

    @Test
    void WHEN_GET_for_clients_THEN_the_endpoint_should_return_OK_and_an_empty_list() throws Exception {

        mvc.perform(get("/clients"))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Nested
    class WHEN_a_valid_body_is_given {

        private static final String GIVEN_REQUEST_BODY = """
                {
                    "firstName": "Adam",
                    "lastName": "Nowak",
                    "email": "adam@nowak.com",
                    "phone": "123 123 1234",
                    "gender": "Male"
                }
                """;

        @Nested
        class AND_when_that_body_is_PUT_under_a_valid_path {

            private final String givenId = Id.<Client>random().toString();

            private ResultActions putResult;

            @BeforeEach
            void when() throws Exception {
                putResult = mvc.perform(put("/clients/" + givenId)
                        .contentType(APPLICATION_JSON)
                        .content(GIVEN_REQUEST_BODY));
            }

            @Test
            void THEN_the_endpoint_should_respond_with_201_and_a_location_header() throws Exception {

                // then
                putResult
                        .andExpect(status().isCreated())
                        .andExpect(header().string(HttpHeaders.LOCATION, "/clients/" + givenId));
            }

            @Test
            void THEN_when_GET_for_clients_the_endpoint_should_return_200_and_an_expected_client() throws Exception {

                // expect
                mvc.perform(get("/clients"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].firstName", is("Adam")))
                        .andExpect(jsonPath("$[0].lastName", is("Nowak")))
                        .andExpect(jsonPath("$[0].email", is("adam@nowak.com")))
                        .andExpect(jsonPath("$[0].phone", is("123 123 1234")))
                        .andExpect(jsonPath("$[0].gender", is("Male")))
                        .andExpect(jsonPath("$[0].banned", is(false)));
            }
        }

        @Nested
        class AND_when_that_body_is_PUT_under_an_invalid_path {

            private ResultActions putResult;

            @BeforeEach
            void when() throws Exception {
                putResult = mvc.perform(put("/clients/invalid_id")
                        .contentType(APPLICATION_JSON)
                        .content(GIVEN_REQUEST_BODY));
            }

            @Test
            void THEN_the_endpoint_should_return_with_a_bad_request_status_and_a_feedback() throws Exception {

                // then
                putResult
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error", is("Constraint Violation")))
                        .andExpect(jsonPath("$.details").isArray())
                        .andExpect(jsonPath("$.details", hasSize(1)))
                        .andExpect(jsonPath("$.details[0].path", is("id")))
                        .andExpect(jsonPath("$.details[0].message", is("must be a valid UUID")));
            }

            @Test
            void THEN_when_GET_for_clients_the_endpoint_should_return_200_and_an_empty_list() throws Exception {
                mvc.perform(get("/clients"))
                        .andExpect(jsonPath("$", hasSize(0)))
                        .andExpect(status().isOk());
            }
        }
    }

    @Test
    void WHEN_PUT_with_an_invalid_body_THEN_the_endpoint_should_return_a_bad_request_status_and_a_feedback() throws Exception {

        var id = Id.<Client>random().toString();

        // expect
        mvc.perform(put("/clients/" + id)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": null,
                                    "lastName": " ",
                                    "email": "adam@",
                                    "phone": "1",
                                    "gender": "?",
                                    "banned": "maybe"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$", hasItem("firstName: must not be blank")))
                .andExpect(jsonPath("$", hasItem("lastName: must not be blank")))
                .andExpect(jsonPath("$", hasItem("email: must be a well-formed email address")))
                .andExpect(jsonPath("$", hasItem("phone: must contain 10 digits or 11 digits starting with 1")))
                .andExpect(jsonPath("$", hasItem("gender: must be a valid gender")))
                .andExpect(jsonPath("$", hasItem("banned: must be either true or false")));
    }
}
