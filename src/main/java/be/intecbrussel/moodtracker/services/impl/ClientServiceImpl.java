package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import be.intecbrussel.moodtracker.services.ClientService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public void addClient(Client client) {

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Client> getAllClients() {
        return null;
    }

    @Override
    public User getCurrentUSer() {
        return null;
    }

    @Override
    public Client updateClient(Client client) {
        return null;
    }

    @Override
    public void deleteClient(Long id) {

    }

}
