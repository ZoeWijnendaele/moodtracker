package be.intecbrussel.moodtracker.controllers;

import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.services.impl.ClientServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/get/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable("id") Long id) {
        Optional<ClientDTO> client = clientService.getClientById(id);

        return ResponseEntity.ok(client.orElseThrow(()
                -> new ResourceNotFoundException("Client", "id", String.valueOf(id))));
    }

    @GetMapping("/get/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/get/current_client")
    public ResponseEntity<ClientDTO> getCurrentClient() {
        return ResponseEntity.ok(clientService.getCurrentClient());
    }

    @PutMapping("/update/client/{email}")
    public ResponseEntity<Client> updateClient(@RequestBody ClientDTO clientDTO, @PathVariable("email") String email) {
        return ResponseEntity.ok(clientService.updateClient(clientDTO, email));
    }

    @PutMapping("/update/profile/{email}")
    public ResponseEntity<Client> updateProfile(@RequestBody ProfileDTO profileDTO, @PathVariable("email") String email) {
        return ResponseEntity.ok(clientService.updateProfile(profileDTO, email));
    }

    @DeleteMapping("/deactivate/{id}")
    public void deactivateClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
    }

}
