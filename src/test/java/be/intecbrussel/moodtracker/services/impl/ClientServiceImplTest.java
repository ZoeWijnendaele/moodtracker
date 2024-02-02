package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.AuthenticationFailureException;
import be.intecbrussel.moodtracker.exceptions.ClientPresentInDatabaseException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.models.mappers.ClientMapper;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.security.JwtUtil;
import be.intecbrussel.moodtracker.services.mergers.ClientMergerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMergerService clientMergerService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private ClientServiceImpl clientService;
    private Client client;
    private ProfileDTO profileDTO;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client(1L, "Username", "email@example.com", "Password", LocalDate.EPOCH, Avatar.DEFAULT);
        profileDTO = new ProfileDTO(2L, "Username", "email@example.com", "Password", LocalDate.EPOCH, Avatar.DEFAULT);
        clientDTO = new ClientDTO(3L, "Username", "email@example.com", "Password");
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    public void givenProfileDTO_WhenAddClient_ThenReturnSavedClient() {
        given(clientRepository.findByEmail(profileDTO.getEmail())).willReturn(Optional.empty());
        given(bCryptPasswordEncoder.encode(profileDTO.getPassword())).willReturn("Password");
        given(clientRepository.save(any(Client.class))).willAnswer(invocation -> invocation.getArgument(0));

        clientService.addClient(profileDTO);

        assertThat(profileDTO).isNotNull();
        assertThat(profileDTO.getPassword()).isEqualTo("Password");
    }

    @Test
    public void givenExistingEmail_WhenAddClient_ThenThrowClientPresentInDatabaseException() {
        given(clientRepository.findByEmail(profileDTO.getEmail())).willReturn(Optional.of(client));

        assertThrows(ClientPresentInDatabaseException.class, () ->
                clientService.addClient(profileDTO));
    }

    @Test
    public void givenClientRepositoryError_WhenAddClient_ThenThrowException() {
        given(clientRepository.findByEmail(profileDTO.getEmail())).willReturn(Optional.empty());
        given(bCryptPasswordEncoder.encode(profileDTO.getPassword())).willReturn("Password");
        given(clientRepository.save(any(Client.class))).willThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> clientService.addClient(profileDTO));
    }

//    @Test
//    public void givenValidLoginRequest_WhenLogin_ThenReturnLoginResponse() {
//        Client clientLogin = new Client(1L, "Username", "email@example.com", "Password", LocalDate.EPOCH, Avatar.DEFAULT);
//
//        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);
//        given(authentication.getName()).willReturn(clientLogin.getEmail());
//        given(clientRepository.findByEmail(clientLogin.getEmail())).willReturn(Optional.of(clientLogin));
//        given(bCryptPasswordEncoder.matches(any(), any())).willReturn(true);
//
//        LoginRequest loginRequest = new LoginRequest("email@example.com", "Password");
//        LoginResponse loginResponse = clientService.login(loginRequest);
//
//        assertThat(loginResponse).isNotNull();
//        assertThat(loginResponse.getEmail()).isEqualTo(clientLogin.getEmail());
//        assertThat(loginResponse.getToken()).isEqualTo("token");
//
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(authentication, atLeastOnce()).getName();
//        verify(clientRepository).findByEmail(clientLogin.getEmail());
//    }

    @Test
    public void givenInvalidLoginRequest_WhenLogin_ThenThrowAuthenticationFailureException() {

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new AuthenticationFailureException("Authentication failed"));

        LoginRequest loginRequest = new LoginRequest("email@example.com", "Password");

        AuthenticationFailureException authenticationFailureException =
                assertThrows(AuthenticationFailureException.class, () ->
                        clientService.login(loginRequest));

        assertThat(authenticationFailureException.getMessage()).isEqualTo("Authentication failed");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(clientRepository, never()).findByEmail(any());
    }

//    @Test
//    public void givenInvalidLoginRequest_WhenLogin_ThenThrowResourceNotFoundException() {
//
//        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .willThrow(AuthenticationException.class);
//
//        LoginRequest loginRequest = new LoginRequest("email@example.com", "Password");
//
//        ResourceNotFoundException resourceNotFoundException =
//                assertThrows(ResourceNotFoundException.class, () ->
//                        clientService.login(loginRequest));
//
//        assertThat(resourceNotFoundException.getMessage())
//                .isEqualTo("Client with email: 'email@example.com' not found in database");
//
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(clientRepository, never()).findByEmail(any());
//    }

    @Test
    public void givenClientID_WhenGetClientById_ThenReturnClientDTO() {
        Long id = 1L;
        Client client = new Client(1L, "Username", "email@example.com", "Password", LocalDate.EPOCH, Avatar.DEFAULT);

        given(clientRepository.findById(id)).willReturn(Optional.of(client));

        Optional<ClientDTO> clientDTO = clientService.getClientById(id);

        assertThat(clientDTO).isPresent();

        assertThat(clientDTO.get().getClientID()).isEqualTo(client.getClientID());
        assertThat(clientDTO.get().getUserName()).isEqualTo(client.getUserName());
        assertThat(clientDTO.get().getEmail()).isEqualTo(client.getEmail());

        verify(clientRepository).findById(id);
    }

    @Test
    public void givenInvalidClientID_WhenGetClientById_ThenThrowResourceNotFoundException() {
        Long invalidId = 1L;
        given(clientRepository.findById(invalidId)).willReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () ->
                        clientService.getClientById(invalidId));

        assertThat(resourceNotFoundException.getMessage())
                .isEqualTo("Client with id: '1' not found in database");
        assertThat(resourceNotFoundException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(clientRepository).findById(invalidId);
    }

    //MEMO: check if I need equals and hashcode in entity and dto
//    @Test
//    public void givenClientDTO_whenGetAllClients_thenReturnListOfClientObject() {
//        List<ClientDTO> clientDTOs = Collections.singletonList(clientDTO);
//        List<Client> clients = clientDTOs.stream().map(ClientMapper::mapClientDTOToClient).collect(Collectors.toList());
//
//        given(clientRepository.findAll()).willReturn(clients);
//
//        List<ClientDTO> result = clientService.getAllClients();
//
//        assertThat(result).isEqualTo(clientDTOs);
//        assertThat(result).isNotEmpty();
//    }
}