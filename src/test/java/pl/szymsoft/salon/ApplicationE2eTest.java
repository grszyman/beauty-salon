package pl.szymsoft.salon;

import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.values.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("An Application")
class ApplicationE2eTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void on_get_should_return_clients_resources() throws Exception {

        mvc.perform(get("/clients"))
                .andExpect(status().isOk());
    }

    @Test
    void on_put_should_save_a_new_client() throws Exception {

        var id = Id.<Client>random().toString();

        // expect
        mvc.perform(put("/clients/" + id)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Adam",
                                    "lastName": "Nowak",
                                    "email": "adam@nowak.com",
                                    "phone": "123 123 1234",
                                    "gender": "Male"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/clients/" + id));
    }
}
