package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import be.intecbrussel.moodtracker.models.enums.Role;
import be.intecbrussel.moodtracker.models.mappers.ClientMapper;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.security.JwtUtil;
import be.intecbrussel.moodtracker.services.ClientService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder BCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ClientServiceImpl(ClientRepository clientRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                             AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.clientRepository = clientRepository;
        BCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addClient(ClientDTO clientDTO) {
        Client client = ClientMapper.mapClientDTOToClient(clientDTO);
        clientRepository.save(client);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String email = authentication.getName();

        Role role = clientRepository.findBy(email)
            .map(Client::getRole)
                .orElseThrow();
        return null;
    }

    @Override
    public Optional<ClientDTO> getClientById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return null;
    }

    @Override
    public User getCurrentUSer() {
        return null;
    }

    @Override
    public Client updateClient(ClientDTO clientDTO) {
        return null;
    }

    @Override
    public void deleteClient(Long id) {

    }

}
