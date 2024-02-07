package be.intecbrussel.moodtracker.services.mergers;

import be.intecbrussel.moodtracker.exceptions.EmailMismatchException;
import be.intecbrussel.moodtracker.exceptions.PasswordMismatchException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.validators.EmailValidator;
import be.intecbrussel.moodtracker.validators.PasswordValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientMergerServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private PasswordValidator passwordValidator;
    @InjectMocks
    private ClientMergerService clientMergerService;
    private Client client;
    private ProfileDTO profileDTO;
    private ClientDTO clientDTO;

    @BeforeEach
    public void setUp() {
        client = new Client(1L, "UserName", "Email", "Password", LocalDate.EPOCH, Avatar.DEFAULT);
        profileDTO = new ProfileDTO(1L, "UserNameClientDTO", "Email@CProfileDTO.com", "PasswordProfileDTO", LocalDate.EPOCH, Avatar.OPTION3);
        clientDTO = new ClientDTO(1L, "UserNameClientDTO", "Email@ClientDTO.com", "PasswordClientDTO");
    }

    @AfterEach
    public void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    public void givenInvalidEmail_WhenMergingProfileData_ThenThrowEmailMisMatchException() {
        String invalidEmail = "invalidEmail";
        profileDTO.setEmail(invalidEmail);

        given(emailValidator.isValid(invalidEmail, null)).willReturn(false);

        EmailMismatchException emailMismatchException = assertThrows(EmailMismatchException.class, () ->
                clientMergerService.mergeProfileData(client.getClientID(), profileDTO));

        assertThat(emailMismatchException.getMessage())
                .isEqualTo("Client with email: 'invalidEmail' not a valid email");

        verify(clientRepository, never()).findByEmail(invalidEmail);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void givenInvalidPassword_WhenMergingProfileData_ThenThrowPasswordMisMatchException() {
        String invalidPassword = "invalidPassword";
        profileDTO.setEmail(client.getEmail());
        profileDTO.setPassword(invalidPassword);

        given(emailValidator.isValid(client.getEmail(), null)).willReturn(true);
        given(passwordValidator.isValid(invalidPassword, null)).willReturn(false);

        PasswordMismatchException passwordMismatchException = assertThrows(PasswordMismatchException.class, () ->
                clientMergerService.mergeProfileData(client.getClientID(), profileDTO));

        assertThat(passwordMismatchException.getMessage())
                .isEqualTo("Client with password: 'invalidPassword' not a valid password");

        verify(clientRepository, never()).findByEmail(invalidPassword);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void givenInvalidEmail_WhenMergingClientData_ThenThrowEmailMisMatchException() {
        String invalidEmail = "invalidEmail";
        clientDTO.setEmail(invalidEmail);

        given(emailValidator.isValid(invalidEmail, null)).willReturn(false);

        EmailMismatchException emailMismatchException = assertThrows(EmailMismatchException.class, () ->
                clientMergerService.mergeClientData(client.getClientID(), clientDTO));

        assertThat(emailMismatchException.getMessage())
                .isEqualTo("Client with email: 'invalidEmail' not a valid email");

        verify(clientRepository, never()).findByEmail(invalidEmail);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void givenInvalidPassword_WhenMergingClientData_ThenThrowPasswordMisMatchException() {
        String invalidPassword = "invalidPassword";
        clientDTO.setEmail(client.getEmail());
        clientDTO.setPassword(invalidPassword);

        given(emailValidator.isValid(client.getEmail(), null)).willReturn(true);
        given(passwordValidator.isValid(invalidPassword, null)).willReturn(false);

        PasswordMismatchException passwordMismatchException = assertThrows(PasswordMismatchException.class, () ->
                clientMergerService.mergeClientData(client.getClientID(), clientDTO));

        assertThat(passwordMismatchException.getMessage())
                .isEqualTo("Client with password: 'invalidPassword' not a valid password");

        verify(clientRepository, never()).findByEmail(invalidPassword);
        verify(clientRepository, never()).save(any(Client.class));
    }

//    @Test
//    public void givenProfileDTOWithNewValues_WhenMergingProfileData_ThenMergeSuccessfully() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.of(client));
//
//        clientMergerService.mergeProfileData(client.getClientID(), profileDTO);
//
//        assertThat(client.getUserName()).isEqualTo(profileDTO.getUserName());
//        assertThat(client.getEmail()).isEqualTo(profileDTO.getEmail());
//        assertThat(client.getPassword()).isEqualTo(profileDTO.getPassword());
//        assertThat(client.getAvatar()).isEqualTo(Avatar.OPTION3);
//
//        verify(clientRepository).findById(client.getClientID());
//    }
//
//    @Test
//    public void givenClientDTOWithNewValues_WhenMergingClientData_ThenMergeSuccessfully() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.of(client));
//
//        clientMergerService.mergeClientData(client.getClientID(), clientDTO);
//
//        assertThat(client.getUserName()).isEqualTo(clientDTO.getUserName());
//        assertThat(client.getEmail()).isEqualTo(clientDTO.getEmail());
//        assertThat(client.getPassword()).isEqualTo(clientDTO.getPassword());
//
//       verify(clientRepository).findById(client.getClientID());
//    }
//
//    @Test
//    public void givenProfileDTOWithSameValues_WhenMergingProfileData_ThenUseExistingData() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.of(client));
//        ProfileDTO sameValuesProfileDTO = new ProfileDTO(1L, "UserNameClientDTO", "Email@ClientDTO.com", "PasswordClientDTO", LocalDate.EPOCH, Avatar.OPTION3);
//
//        clientMergerService.mergeProfileData(client.getClientID(), sameValuesProfileDTO);
//
//        assertThat(client.getUserName()).isEqualTo("UserNameClientDTO");
//        assertThat(client.getEmail()).isEqualTo("Email@ClientDTO.com");
//        assertThat(client.getPassword()).isEqualTo("PasswordClientDTO");
//        assertThat(client.getAvatar()).isEqualTo(Avatar.OPTION3);
//
//        verify(clientRepository).findById(client.getClientID());
//    }
//
//    @Test
//    public void givenClientDTOWithSameValues_WhenMergingClientData_ThenUseExistingData() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.of(client));
//        ClientDTO sameValuesClientDTO = new ClientDTO(1L, "UserNameClientDTO", "Email@ClientDTO.com", "NewPasswordClientDTO");
//
//        clientMergerService.mergeClientData(client.getClientID(), sameValuesClientDTO);
//
//        assertThat(client.getUserName()).isEqualTo("UserNameClientDTO");
//        assertThat(client.getEmail()).isEqualTo("Email@ClientDTO.com");
//        assertThat(client.getPassword()).isEqualTo("NewPasswordClientDTO");
//
//        verify(clientRepository).findById(client.getClientID());
//    }
//
//    @Test
//    public void givenInvalidClientID_WhenMergingClientData_ThenThrowResourceNotFoundException() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.empty());
//
//        ResourceNotFoundException resourceNotFoundException =
//                assertThrows(ResourceNotFoundException.class, () ->
//                        clientMergerService.mergeClientData(client.getClientID(), clientDTO));
//
//        assertThat(resourceNotFoundException.getMessage())
//                .isEqualTo("Client with ID: '1' not found in database");
//        assertThat(resourceNotFoundException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
//
//        verify(clientRepository).findById(client.getClientID());
//    }
//
//    @Test
//    public void givenInvalidProfileID_WhenMergingProfileData_ThenThrowResourceNotFoundException() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.empty());
//
//        ResourceNotFoundException resourceNotFoundException =
//                assertThrows(ResourceNotFoundException.class, () ->
//                        clientMergerService.mergeProfileData(client.getClientID(), profileDTO));
//
//        assertThat(resourceNotFoundException.getMessage())
//                .isEqualTo("Client with ID: '1' not found in database");
//        assertThat(resourceNotFoundException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
//
//        verify(clientRepository).findById(client.getClientID());
//    }
//
//    @Test
//    public void givenInvalidValues_whenMergingProfileData_thenThrowMergeFailureException() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.of(client));
//        given(clientRepository.save(any(Client.class))).willThrow(MergeFailureException.class);
//
//        assertThatThrownBy(() -> clientMergerService.mergeProfileData(client.getClientID(), profileDTO))
//                .isInstanceOf(MergeFailureException.class)
//                .hasMessageContaining("Failed to merge profile data");
//
//        verify(clientRepository).findById(client.getClientID());
//        verify(clientRepository).save(any(Client.class));
//    }
//
//    @Test
//    public void givenInvalidValues_whenMergingClientData_thenThrowMergeFailureException() {
//        given(clientRepository.findById(client.getClientID())).willReturn(Optional.of(client));
//        given(clientRepository.save(any(Client.class))).willThrow(MergeFailureException.class);
//
//        assertThatThrownBy(() -> clientMergerService.mergeClientData(client.getClientID(), clientDTO))
//                .isInstanceOf(MergeFailureException.class)
//                .hasMessageContaining("Failed to merge client data");
//
//        verify(clientRepository).findById(client.getClientID());
//        verify(clientRepository).save(any(Client.class));
//    }
}
