package pl.szymsoft.salon.root;

import pl.szymsoft.salon.appointment.adapters.out.memory.AppointmentsInMemoryAdapter;
import pl.szymsoft.salon.appointment.domain.Appointment;
import pl.szymsoft.salon.appointment.domain.ports.Appointments;
import pl.szymsoft.salon.client.adapters.out.memory.ClientsInMemoryAdapter;
import pl.szymsoft.salon.client.domain.Client;
import pl.szymsoft.salon.client.domain.ports.Clients;
import pl.szymsoft.salon.values.FirstName;
import pl.szymsoft.salon.values.Id;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static pl.szymsoft.salon.root.RootRestControllerIntegrationTest.TestConfiguration;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = TestConfiguration.class)
class RootRestControllerIntegrationTest {

    @Configuration
    @Import(RootRestConfiguration.class)
    static class TestConfiguration {

        @Bean
        Clients clients() {
            return new ClientsInMemoryAdapter();
        }

        @Bean
        Appointments appointments() {
            return new AppointmentsInMemoryAdapter();
        }
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Clients clients;

    @Autowired
    private Appointments appointments;

    @Test
    void should_save_given_client_and_appointment_when_valid_files_posted_on_root_path() throws Exception {

        // given
        var clientsFile = new MockMultipartFile("clients", "clients.csv", MediaType.TEXT_PLAIN_VALUE, """
                id,first_name,last_name,email,phone,gender,banned
                e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false
                """.getBytes(UTF_8));

        var appointmentFile = new MockMultipartFile("appointments", "appointments.csv", MediaType.TEXT_PLAIN_VALUE, """
                id,client_id,start_time,end_time
                67ce894a-9625-4ab7-8b91-17d83fb3fd10,e0b8ebfc-6e57-4661-9546-328c644a3764,2017-05-09 15:30:00 +0100,2017-05-09 18:30:00 +0100
                """.getBytes(UTF_8));

        var servicesFile = new MockMultipartFile("services", "services.csv", MediaType.TEXT_PLAIN_VALUE, """
                id,appointment_id,name,price,loyalty_points
                a5534117-feee-4c13-a238-bb76a19bf22d,67ce894a-9625-4ab7-8b91-17d83fb3fd10,Pedicure,46.0,40
                """.getBytes(UTF_8));

        var purchasesFile = new MockMultipartFile("purchases", "purchases.csv", MediaType.TEXT_PLAIN_VALUE, """
                id,appointment_id,name,price,loyalty_points
                187def47-f9cb-41a3-9041-e2c7d2130e6e,67ce894a-9625-4ab7-8b91-17d83fb3fd10,Body Wash,11.0,10
                """.getBytes(UTF_8));

        // when
        mvc.perform(multipart("/")
                        .file(clientsFile)
                        .file(appointmentFile)
                        .file(servicesFile)
                        .file(purchasesFile))
                .andExpect(status().isOk());

        // then
        Assertions.assertThat(clients.findAll())
                .anySatisfy(client -> Assertions.assertThat(client)
                        .as("id does match").returns(Id.of("e0b8ebfc-6e57-4661-9546-328c644a3764"), Client::getId)
                        .as("firstName does match").returns(FirstName.of("Dori"), Client::getFirstName));

        assertThat(appointments.findByClientId(Id.of("e0b8ebfc-6e57-4661-9546-328c644a3764")))
                .first()
                .as("").returns(50L, Appointment::getLoyaltyPoints);
    }
}