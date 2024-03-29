package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.*;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.LoginRequest;
import be.intecbrussel.moodtracker.models.dtos.LoginResponse;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.security.JwtUtil;
import be.intecbrussel.moodtracker.services.mergers.ClientMergerService;
import be.intecbrussel.moodtracker.validators.EmailValidator;
import be.intecbrussel.moodtracker.validators.PasswordValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    @Mock
    private PasswordValidator passwordValidator;
    @Mock
    private EmailValidator emailValidator;
    @InjectMocks
    private ClientServiceImpl clientService;
    private Client client;
    private ProfileDTO profileDTO;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client(1L, "Username", "email@example.com", "Password123?", LocalDate.EPOCH, Avatar.DEFAULT);
        profileDTO = new ProfileDTO(1L, "Username", "email@example.com", "Password123?", LocalDate.EPOCH, Avatar.DEFAULT);
        clientDTO = new ClientDTO(1L, "Username", "email@example.com", "Password123?");
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
        Mockito.reset(clientRepository, clientMergerService, bCryptPasswordEncoder, authenticationManager, jwtUtil, passwordValidator, emailValidator);
    }

    @Test
    public void givenProfileDTO_whenAddClient_thenReturnSavedClient() {
        given(passwordValidator.isValid(client.getPassword(), null)).willReturn(true);
        given(emailValidator.isValid(profileDTO.getEmail(), null)).willReturn(true);
        given(clientRepository.findByEmail(profileDTO.getEmail())).willReturn(Optional.empty());
        given(bCryptPasswordEncoder.encode(profileDTO.getPassword())).willReturn("Password123?");
        given(clientRepository.save(any(Client.class))).willAnswer(invocation -> invocation.getArgument(0));

        clientService.addClient(profileDTO);

        assertThat(profileDTO).isNotNull();
        assertThat(profileDTO.getPassword()).isEqualTo("Password123?");
    }

    @Test
    public void givenExistingEmail_whenAddClient_thenThrowPresentInDatabaseException() {
        String existingEmail = "email@example.com";
        profileDTO.setEmail(existingEmail);
        profileDTO.setPassword(client.getPassword());

        given(passwordValidator.isValid(client.getPassword(), null)).willReturn(true);
        given(emailValidator.isValid(existingEmail, null)).willReturn(true);

        given(clientRepository.findByEmail(profileDTO.getEmail())).willReturn(Optional.of(client));

        PresentInDatabaseException presentInDatabaseException =
                assertThrows(PresentInDatabaseException.class, () ->
                        clientService.addClient(profileDTO));

        assertThat(presentInDatabaseException.getMessage())
                .isEqualTo("Client with email: email@example.com already exists");

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void givenClientRepositoryError_WhenAddClient_ThenThrowException() {
        given(clientRepository.findByEmail(profileDTO.getEmail())).willReturn(Optional.empty());
        given(bCryptPasswordEncoder.encode(profileDTO.getPassword())).willReturn("Password123?");
        given(clientRepository.save(any(Client.class))).willThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> clientService.addClient(profileDTO));
    }

    @Test
    public void givenInvalidEmail_WhenAddClient_ThenThrowEmailMismatchException() {
        String invalidEmail = "invalidEmail";
        profileDTO.setEmail(invalidEmail);
        profileDTO.setPassword(client.getPassword());

        given(emailValidator.isValid(invalidEmail, null)).willReturn(false);
        given(passwordValidator.isValid(client.getPassword(), null)).willReturn(true);

        EmailMismatchException emailMismatchException = assertThrows(EmailMismatchException.class, () ->
                clientService.addClient(profileDTO));

        assertThat(emailMismatchException.getMessage())
                .isEqualTo("Client with email: 'invalidEmail' not a valid email");

        verify(clientRepository, never()).findByEmail(invalidEmail);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void givenInvalidPassword_WhenAddClient_ThenThrowPasswordMismatchException() {
        String invalidPassword = "invalidPassword";
        profileDTO.setEmail(client.getEmail());
        profileDTO.setPassword(invalidPassword);

        given(emailValidator.isValid(client.getEmail(), null)).willReturn(true);
        given(passwordValidator.isValid(invalidPassword, null)).willReturn(false);

        PasswordMismatchException passwordMismatchException = assertThrows(PasswordMismatchException.class, () ->
                clientService.addClient(profileDTO));

        assertThat(passwordMismatchException.getMessage()).isEqualTo("Client with password: 'invalidPassword' not a valid password");

        verify(bCryptPasswordEncoder, never()).encode(anyString());
        verify(clientRepository, never()).save(any(Client.class));
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
    public void givenClientID_whenGetClientById_thenReturnClientDTO() {
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
    public void givenInvalidClientID_whenGetClientById_thenThrowResourceNotFoundException() {
        Long invalidId = 1L;
        given(clientRepository.findById(invalidId)).willReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () ->
                        clientService.getClientById(invalidId));

        assertThat(resourceNotFoundException.getMessage())
                .isEqualTo("Client with id: '1' not found in database");

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
    public void givenClientDTO_whenUpdateClient_thenReturnUpdatedClientDTO() {
        String authenticatedEmail = "email@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(anyString())).willReturn(Optional.of(client));
        given(clientRepository.save(any(Client.class))).willAnswer(invocation -> invocation.getArgument(0));

        Client updatedClient = clientService.updateClient(clientDTO, authenticatedEmail);

        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getClientID()).isEqualTo(clientDTO.getClientID());
        assertThat(updatedClient.getUserName()).isEqualTo(clientDTO.getUserName());
        assertThat(updatedClient.getEmail()).isEqualTo(clientDTO.getEmail());
        assertThat(updatedClient.getPassword()).isEqualTo(clientDTO.getPassword());

        verify(clientMergerService, times(1)).mergeClientData(client.getClientID(), clientDTO);
        verify(clientRepository, times(1)).findByEmail(authenticatedEmail);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void givenInvalidClientID_whenUpdateClient_thenReturnResourceNotFoundException() {
        String invalidEmail = "invalid@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(invalidEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(invalidEmail)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(clientDTO, invalidEmail));

        verify(clientRepository, times(1)).findByEmail(invalidEmail);
        verify(clientRepository, never()).save(any());
    }

    @Test
    public void givenMergeFailureClientDTO_whenUpdateClient_thenReturnMergeFailureException() {
        String authenticatedEmail = "email@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(authenticatedEmail)).willReturn(Optional.of(client));

        doThrow(new MergeFailureException("Client", "email", authenticatedEmail))
                .when(clientMergerService).mergeClientData(client.getClientID(), clientDTO);

        MergeFailureException mergeFailureException =
                assertThrows(MergeFailureException.class, () ->
                        clientService.updateClient(clientDTO, authenticatedEmail));

        assertThat(mergeFailureException.getMessage())
                .isEqualTo("Client with email: 'email@example.com' not able to merge");

        verify(clientRepository, times(1)).findByEmail(authenticatedEmail);
        verify(clientMergerService, times(1)).mergeClientData(client.getClientID(), clientDTO);
        verify(clientRepository, never()).save(any());
    }

    @Test
    public void givenExceptionClientDTO_whenUpdateClient_thenReturnAuthenticationFailureException() {
        String authenticatedEmail = "email@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(authenticatedEmail)).willReturn(Optional.of(client));
        given(clientRepository.save(any(Client.class))).willThrow(RuntimeException.class);

        assertThrows(AuthenticationFailureException.class, () -> clientService.updateClient(clientDTO, authenticatedEmail));

        verify(clientRepository, times(1)).findByEmail(authenticatedEmail);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void givenValidProfileDTO_whenUpdateProfile_thenReturnProfileDTO() {
        String authenticatedEmail = "email@example.com";
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        given(clientRepository.findByEmail(authenticatedEmail)).willReturn(Optional.of(client));
        given(clientRepository.save(any(Client.class))).willAnswer(invocation -> invocation.getArgument(0));

        Client updatedClient = clientService.updateProfile(profileDTO, authenticatedEmail);

        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getClientID()).isEqualTo(profileDTO.getClientID());
        assertThat(updatedClient.getUserName()).isEqualTo(profileDTO.getUserName());
        assertThat(updatedClient.getEmail()).isEqualTo(profileDTO.getEmail());
        assertThat(updatedClient.getPassword()).isEqualTo(profileDTO.getPassword());
        assertThat(updatedClient.getAvatar()).isEqualTo(profileDTO.getAvatar());

        verify(clientMergerService, times(1)).mergeProfileData(client.getClientID(), profileDTO);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void givenInvalidProfileID_whenUpdateProfile_thenReturnResourceNotFoundException() {
        String invalidEmail = "invalid@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(invalidEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(invalidEmail)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.updateProfile(profileDTO, invalidEmail));

        verify(clientRepository, times(1)).findByEmail(invalidEmail);
        verify(clientRepository, never()).save(any());
    }

    @Test
    public void givenMergeFailureProfileDTO_whenUpdateClient_thenReturnMergeFailureException() {
        String authenticatedEmail = "email@example.com";
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        ProfileDTO profileDTO = new ProfileDTO(1L, "UpdatedUsername", "updated@example.com", "UpdatedPassword", null, null);
        given(clientRepository.findByEmail(authenticatedEmail)).willReturn(Optional.of(client));

        doThrow(new MergeFailureException("Profile", "email", authenticatedEmail))
                .when(clientMergerService).mergeProfileData(client.getClientID(), profileDTO);

        assertThrows(MergeFailureException.class, () -> clientService.updateProfile(profileDTO, authenticatedEmail));

        verify(clientMergerService, times(1)).mergeProfileData(client.getClientID(), profileDTO);
    }

    @Test
    public void givenUnInvalidProfileDTO_whenUpdateClient_thenReturnAuthenticationFailureException() {
        String authenticatedEmail = "email@example.com";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedEmail, "Password"));
        SecurityContextHolder.setContext(securityContext);

        given(clientRepository.findByEmail(authenticatedEmail)).willReturn(Optional.of(client));
        given(clientRepository.save(any(Client.class))).willThrow(RuntimeException.class);

        assertThrows(AuthenticationFailureException.class, () -> clientService.updateProfile(profileDTO, authenticatedEmail));

        verify(clientRepository, times(1)).findByEmail(authenticatedEmail);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void givenClientID_whenDeleteClient_thenReturnVoid() {
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