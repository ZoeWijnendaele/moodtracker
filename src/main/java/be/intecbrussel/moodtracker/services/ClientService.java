package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    void addClient(ProfileDTO profileDTO);
    LoginResponse login(LoginRequest loginRequest);
    Optional<ClientDTO> getClientById(Long id);
    Optional<Client> getClientByIdForMood(Long id);
    List<ClientDTO> getAllClients();
    ClientDTO getCurrentClient();
    Client updateClient(ClientDTO clientDTO, String email);
    Client updateProfile(ProfileDTO profileDTO, String email);
    void deleteClient(Long id);

}
