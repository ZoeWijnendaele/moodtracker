package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.models.enums.Role;
import be.intecbrussel.moodtracker.models.mappers.ClientMapper;
import be.intecbrussel.moodtracker.models.mappers.ProfileMapper;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.security.JwtUtil;
import be.intecbrussel.moodtracker.services.ClientService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    public void addClient(ProfileDTO profileDTO) {
        Client client = ProfileMapper.mapProfileDTOToProfile(profileDTO);
        clientRepository.save(client);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String email = authentication.getName();

        Role role = clientRepository.findByEmail(email)
                .map(Client::getRole)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));

        Client client = new Client(email, "", role);
        String jwtToken = jwtUtil.createAccessToken(client);

        return new LoginResponse(email, jwtToken);
    }

    @Override
    public Optional<ClientDTO> getClientById(Long id) {

        return clientRepository.findById(id)
                .map(ClientMapper::mapClientToClientDTO)
                .map(Optional::of)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", String.valueOf(id)));
    }

    @Override
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .map(ClientMapper::mapClientToClientDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getCurrentUSer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));

        return ClientMapper.mapClientToClientDTO(client);
    }

    @Override
    public Client updateClient(ClientDTO clientDTO) {
        return null;
    }

    @Override
    public Client updateProfile(ProfileDTO profileDTO) {
        return null;
    }


    @Override
    public void deleteClient(Long id) {

    }

}
