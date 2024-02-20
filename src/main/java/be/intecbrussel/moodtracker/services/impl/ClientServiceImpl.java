package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.*;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.*;
import be.intecbrussel.moodtracker.models.mappers.ClientMapper;
import be.intecbrussel.moodtracker.models.mappers.ProfileMapper;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.security.JwtUtil;
import be.intecbrussel.moodtracker.services.ClientService;
import be.intecbrussel.moodtracker.services.mergers.ClientMergerService;
import be.intecbrussel.moodtracker.validators.EmailValidator;
import be.intecbrussel.moodtracker.validators.PasswordValidator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMergerService clientMergerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;
    private final JwtUtil jwtUtil;
    private final CalendarServiceImpl calendarService;

    public ClientServiceImpl(ClientRepository clientRepository,
                             ClientMergerService clientMergerService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             AuthenticationManager authenticationManager,
                             JwtUtil jwtUtil,
                             PasswordValidator passwordValidator,
                             EmailValidator emailValidator,
                             CalendarServiceImpl calendarService) {
        this.clientRepository = clientRepository;
        this.clientMergerService = clientMergerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
        this.calendarService = calendarService;
    }

    @Override
    public void addClient(ProfileDTO profileDTO) {
        String email = profileDTO.getEmail();
        String password = profileDTO.getPassword();

        if (!emailValidator.isValid(email, null)) {
            throw new EmailMismatchException("Client", "email", email);
        }

        if (!passwordValidator.isValid(password, null)) {
            throw new PasswordMismatchException("Client", "password", password);
        }

        if (clientRepository.findByEmail(email).isPresent()) {
            throw new PresentInDatabaseException("Client", "email", email);
        }

        Client client = ProfileMapper.mapProfileDTOToProfile(profileDTO);
        String encodedPassword = bCryptPasswordEncoder.encode(profileDTO.getPassword());
        client.setPassword(encodedPassword);

        client.setRole(Collections.singletonList("USER"));

        clientRepository.save(client);

        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setDateTime(LocalDateTime.now());
        calendarDTO.setClientID(client.getClientID());
        calendarService.addCalendar(calendarDTO);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            String email = authentication.getName();

            Client client = clientRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));

            String jwtToken = jwtUtil.createAccessToken(client);

            return new LoginResponse(email, jwtToken);
        } catch (AuthenticationFailureException e) {
            throw new AuthenticationFailureException("Invalid email or password");
        }
    }

    @Override
    public Optional<ClientDTO> getClientById(Long id) {

        return clientRepository.findById(id)
                .map(ClientMapper::mapClientToClientDTO)
                .map(Optional::of)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", String.valueOf(id)));
    }

    @Override
    public Optional<Client> getClientByIdForMood(Long id) {
        return clientRepository.findById(id)
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
    public ClientDTO getCurrentClient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return clientRepository.findByEmail(email)
                .map(ClientMapper::mapClientToClientDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));
    }

    @Override
    public Client updateClient(ClientDTO clientDTO, String email) {

        try {
            Client client = clientRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));

            clientMergerService.mergeClientData(client.getClientID(), clientDTO);

            if (clientDTO.getPassword() != null) {
                String encryptedPassword = bCryptPasswordEncoder.encode(clientDTO.getPassword());
                client.setPassword(encryptedPassword);
            }


            return clientRepository.save(client);
        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new ResourceNotFoundException("Client", "email", email);
        } catch (MergeFailureException mergeFailureException) {
            throw new MergeFailureException("Client", "email", email);
        } catch (RuntimeException runtimeException) {
            throw new AuthenticationFailureException("Authentication failure");
        }
    }

    @Override
    public Client updateProfile(ProfileDTO profileDTO, String email) {

        try {
            Client clientProfile = clientRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "email", email));

            clientMergerService.mergeProfileData(clientProfile.getClientID(), profileDTO);

            if (profileDTO.getPassword() != null) {
                String encryptedPassword = bCryptPasswordEncoder.encode(profileDTO.getPassword());
                clientProfile.setPassword(encryptedPassword);
            }

            return clientRepository.save(clientProfile);
        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new ResourceNotFoundException("Client", "email", email);
        } catch (MergeFailureException mergeFailureException) {
            throw new MergeFailureException("Client", "email", email);
        } catch (RuntimeException runtimeException) {
            throw new AuthenticationFailureException("Authentication failure");
        }
    }

    //TODO: when deleting a client, also delete all associated moods and calendar
    @Override
    public void deleteClient(Long id) {

        try {
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new ResourceNotFoundException("Client", "id", String.valueOf(id));
        }
    }

}
