package be.intecbrussel.moodtracker.services.mergers;

import be.intecbrussel.moodtracker.exceptions.EmailMismatchException;
import be.intecbrussel.moodtracker.exceptions.MergeFailureException;
import be.intecbrussel.moodtracker.exceptions.PasswordMismatchException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import be.intecbrussel.moodtracker.validators.EmailValidator;
import be.intecbrussel.moodtracker.validators.PasswordValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientMergerService {

    private final ClientRepository clientRepository;
    private final FieldMergerService fieldMergerService;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;

    public ClientMergerService(ClientRepository clientRepository,
                               FieldMergerService fieldMergerService,
                               PasswordValidator passwordValidator,
                               EmailValidator emailValidator) {
        this.fieldMergerService = fieldMergerService;
        this.clientRepository = clientRepository;
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
    }

    private void validateEmail(String email) {
        if (!emailValidator.isValid(email, null)) {
            throw new EmailMismatchException("Client", "email", email);
        }
    }

    private void validatePassword(String password) {
        if (!passwordValidator.isValid(password, null)) {
            throw new PasswordMismatchException("Client", "password", password);
        }
    }

    public void mergeProfileData(Long clientID, ProfileDTO profileDTO) {
        validateEmail(profileDTO.getEmail());
        validatePassword(profileDTO.getPassword());
        Optional<Client> optionalClient = clientRepository.findById(clientID);

        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();

            try {
                fieldMergerService.mergeFieldIfNotNullAndDifferent(profileDTO.getUserName(),
                        ProfileDTO::getUserName, existingClient::setUserName, profileDTO);
               fieldMergerService.mergeFieldIfNotNullAndDifferent(profileDTO.getEmail(),
                        ProfileDTO::getEmail, existingClient::setEmail, profileDTO);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(profileDTO.getPassword(),
                        ProfileDTO::getPassword, existingClient::setPassword, profileDTO);
                Optional.ofNullable(profileDTO.getAvatar())
                        .filter(avatar -> avatar != Avatar.NO_CHANGE && !avatar.equals(existingClient.getAvatar()))
                        .ifPresent(existingClient::setAvatar);
            } catch (MergeFailureException mergeFailureException) {
                throw new MergeFailureException("Client", "email", existingClient.getEmail());
            }
        } else {
            throw new ResourceNotFoundException("Client", "ID", String.valueOf(clientID));
        }
    }

    public void mergeClientData(Long clientID, ClientDTO clientDTO) {
        validateEmail(clientDTO.getEmail());
        validatePassword(clientDTO.getPassword());
        Optional<Client> optionalClient = clientRepository.findById(clientID);

        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();

            try {
                fieldMergerService.mergeFieldIfNotNullAndDifferent(clientDTO.getUserName(),
                        ClientDTO::getUserName, existingClient::setUserName, clientDTO);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(clientDTO.getEmail(),
                        ClientDTO::getEmail, existingClient::setEmail, clientDTO);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(clientDTO.getPassword(),
                        ClientDTO::getPassword, existingClient::setPassword, clientDTO);
            } catch (MergeFailureException mergeFailureException) {
                throw new MergeFailureException("Client", "email", existingClient.getEmail());
            }
        } else {
            throw new ResourceNotFoundException("Client", "ID", String.valueOf(clientID));
        }
    }

}
