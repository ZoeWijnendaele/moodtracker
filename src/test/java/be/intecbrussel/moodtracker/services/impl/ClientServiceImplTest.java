package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.AuthenticationFailureException;
import be.intecbrussel.moodtracker.exceptions.ClientPresentInDatabaseException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

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

    @Test
    public void givenValidLoginRequest_WhenLogin_ThenReturnLoginResponse() {
        authentication = mock(Authentication.class);

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(clientRepository.findByEmail(any())).willReturn(Optional.of(client));
        given(jwtUtil.createAccessToken(client)).willReturn("Mock_token");

        LoginRequest loginRequest = new LoginRequest(client.getEmail(), client.getPassword());
        clientService.login(loginRequest);

        LoginResponse loginResponse = new LoginResponse(loginRequest.getEmail(), "Mock_token");

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getEmail()).isEqualTo("email@example.com");
        assertThat(loginResponse.getToken()).isEqualTo("Mock_token");

        verify(jwtUtil, times(1)).createAccessToken(client);
    }

    @Test
    public void givenInvalidLoginRequest_WhenLogin_ThenThrowAuthenticationFailureException() {

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new AuthenticationFailureException("Invalid email or password"));

        LoginRequest loginRequest = new LoginRequest("email@example.com", "Password");

        AuthenticationFailureException authenticationFailureException =
                assertThrows(AuthenticationFailureException.class, () ->
                        clientService.login(loginRequest));

        assertThat(authenticationFailureException.getMessage()).isEqualTo("Invalid email or password");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(clientRepository, never()).findByEmail(any());
    }

    @Test
    public void givenInvalidLoginRequest_WhenLogin_ThenThrowResourceNotFoundException() {
        String invalidEmail = "invalid@example.com";
        String invalidPassword = "invalidPassword";
        LoginRequest invalidLoginRequest = new LoginRequest(invalidEmail, invalidPassword);

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new AuthenticationFailureException("Invalid email or password"));

        assertThrows(AuthenticationFailureException.class, () -> clientService.login(invalidLoginRequest));

        verify(authenticationManager).authenticate(
                argThat(authenticationToken ->
                        authenticationToken.getPrincipal().equals(invalidEmail) &&
                                authenticationToken.getCredentials().equals(invalidPassword)));
    }

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

    @Test
    public void givenClientDTO_whenGetAllClients_thenReturnListOfClientObject() {
        Client client1 = new Client(1L, "Username", "email@example.com", "Password");
        Client client2 = new Client(2L, "Username", "email@example.com", "Password");
        List<Client> clients = List.of(client1, client2);

        given(clientRepository.findAll()).willReturn(clients);

        List<ClientDTO> result = clientService.getAllClients();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(clients.size());

        assertThat(result.get(0).getClientID()).isEqualTo(client1.getClientID());
        assertThat(result.get(0).getUserName()).isEqualTo(client1.getUserName());
        assertThat(result.get(0).getEmail()).isEqualTo(client1.getEmail());

        assertThat(result.get(1).getClientID()).isEqualTo(client2.getClientID());
        assertThat(result.get(1).getUserName()).isEqualTo(client2.getUserName());
        assertThat(result.get(1).getEmail()).isEqualTo(client2.getEmail());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void givenAuthenticatedClient_whenGetCurrentClient_thenReturnClientDTO() {
        String authenticatedEmail = "email@example.com";
        Client authenticatedClient = new Client(1L, "Username", authenticatedEmail, "Password");
        SecurityContext securityContextHolder = SecurityContextHolder.createEmptyContext();
        securityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        SecurityContextHolder.setContext(securityContextHolder);

        given(clientRepository.findByEmail(anyString())).willReturn(Optional.of(authenticatedClient));

        ClientDTO result = clientService.getCurrentClient();

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(authenticatedEmail);
    }

    @Test
    public void givenInvalidAuthenticatedClient_whenGetCurrentClient_thenReturnAuthenticationFailureException() {
        String authenticatedEmail = "email@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getCurrentClient())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Client with email: 'email@example.com' not found in database");
    }

    @Test
    public void givenClientID_whenDeleteClient_thenReturnEmpty() {
        willDoNothing().given(clientRepository).deleteById(client.getClientID());
        clientService.deleteClient(client.getClientID());

        verify(clientRepository, times(1)).deleteById(client.getClientID());
    }

    @Test
    public void givenInvalidClientID_whenDeleteClient_thenReturnResourceNotFoundException() {
        Long invalidId = 99L;
        willThrow(new EmptyResultDataAccessException(0)).given(clientRepository).deleteById(invalidId);

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () ->
                        clientService.deleteClient(invalidId));

        assertThat(resourceNotFoundException.getMessage()).isEqualTo("Client with id: '99' not found in database");
        assertThat(resourceNotFoundException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(clientRepository, times(1)).deleteById(invalidId);
    }

}