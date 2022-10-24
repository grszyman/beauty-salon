package pl.szymsoft.salon.client.adapters.in.rest;

import pl.szymsoft.salon.client.adapters.in.rest.validators.Uuid;
import pl.szymsoft.salon.client.domain.ports.Clients;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RequestMapping("/clients")
@RequiredArgsConstructor
@Validated
class ClientRestController {

    @NonNull
    private final Clients clients;

    @GetMapping
    public HttpEntity<?> clients() {

        var allClients = clients.findAll();

        return ResponseEntity.ok()
                .body(allClients);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> putUnderId(
            @NotNull @Uuid @PathVariable String id,
            @Valid @RequestBody ClientPutRequest request
    ) {

        var clientToSave = request.toClient(id);
        var savedClient = clients.save(clientToSave);

        return ResponseEntity.created(URI.create("/clients/" + id)).build(); // TODO: temp uri creation
    }
}
