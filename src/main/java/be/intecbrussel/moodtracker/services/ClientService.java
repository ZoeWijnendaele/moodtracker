package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    void addClient(ClientDTO clientDTO);
    LoginResponse login(LoginRequest loginRequest);
    Optional<ClientDTO> getClientById(Long id);
    List<ClientDTO> getAllClients();
    User getCurrentUSer();
    Client updateClient(ClientDTO clientDTO);
    void deleteClient(Long id);

}
