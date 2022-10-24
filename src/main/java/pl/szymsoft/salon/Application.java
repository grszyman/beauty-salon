package pl.szymsoft.salon;

import pl.szymsoft.salon.appointment.adapters.out.jdbc.AppointmentsJdbcAdapterSpringConfiguration;
import pl.szymsoft.salon.client.adapters.in.rest.ClientRestConfiguration;
import pl.szymsoft.salon.client.adapters.out.jdbc.ClientsJdbcAdapterSpringConfiguration;
import pl.szymsoft.salon.rest.RestConfiguration;
import pl.szymsoft.salon.root.RootRestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ImportAutoConfiguration(classes = {
        RootRestConfiguration.class,
        ClientRestConfiguration.class,
        ClientsJdbcAdapterSpringConfiguration.class,
        AppointmentsJdbcAdapterSpringConfiguration.class,
        RestConfiguration.class
})
@ComponentScan(excludeFilters = {
        @Filter(RestController.class),
        @Filter(Configuration.class)
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
