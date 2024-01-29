package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    void addClient(Client client);
    LoginResponse login(LoginRequest loginRequest);
    Optional<Client> getClientById(Long id);
    List<Client> getAllClients();
    User getCurrentUSer();
    Client updateClient(Client client);
    void deleteClient(Long id);

}
