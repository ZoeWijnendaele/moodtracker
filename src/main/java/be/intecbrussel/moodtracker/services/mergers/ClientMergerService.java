package be.intecbrussel.moodtracker.services.mergers;

import be.intecbrussel.moodtracker.exceptions.EmailValidateFailureException;
import be.intecbrussel.moodtracker.exceptions.MergeFailureException;
import be.intecbrussel.moodtracker.exceptions.PasswordValidateFailureException;
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
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class ClientMergerService {

    private final ClientRepository clientRepository;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;

    public ClientMergerService(ClientRepository clientRepository,
                               PasswordValidator passwordValidator,
                               EmailValidator emailValidator) {
        this.clientRepository = clientRepository;
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
    }

    private <T> void mergeFieldIfNotNullAndDifferent(
            String newFieldValue, Function<T, String> getter, Consumer<String> setter, T existingDto) {
        String existingValue = getter.apply(existingDto);

        if (newFieldValue != null && !newFieldValue.isEmpty() && !newFieldValue.equals(existingValue)) {
            setter.accept(newFieldValue);
        }
    }

    public void mergeProfileData(Long clientID, ProfileDTO profileDTO) {

        if (!emailValidator.isValid(profileDTO.getEmail(), null)) {
            throw new EmailValidateFailureException("Client", "email", profileDTO.getEmail());
        }

        if (!passwordValidator.isValid(profileDTO.getPassword(), null)) {
            throw new PasswordValidateFailureException("Client", "password", profileDTO.getPassword());
        }

        Optional<Client> optionalClient = clientRepository.findById(clientID);

        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();

            try {
                mergeFieldIfNotNullAndDifferent(profileDTO.getUserName(),
                        ProfileDTO::getUserName, existingClient::setUserName, profileDTO);
                mergeFieldIfNotNullAndDifferent(profileDTO.getEmail(),
                        ProfileDTO::getEmail, existingClient::setEmail, profileDTO);
                mergeFieldIfNotNullAndDifferent(profileDTO.getPassword(),
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

        if (!emailValidator.isValid(clientDTO.getEmail(), null)) {
            throw new EmailValidateFailureException("Client", "email", clientDTO.getEmail());
        }

        if (!passwordValidator.isValid(clientDTO.getPassword(), null)) {
            throw new PasswordValidateFailureException("Client", "password", clientDTO.getPassword());
        }

        Optional<Client> optionalClient = clientRepository.findById(clientID);

        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();

            try {
                mergeFieldIfNotNullAndDifferent(clientDTO.getUserName(),
                        ClientDTO::getUserName, existingClient::setUserName, clientDTO);
                mergeFieldIfNotNullAndDifferent(clientDTO.getEmail(),
                        ClientDTO::getEmail, existingClient::setEmail, clientDTO);
                mergeFieldIfNotNullAndDifferent(clientDTO.getPassword(),
                        ClientDTO::getPassword, existingClient::setPassword, clientDTO);
            } catch (MergeFailureException mergeFailureException) {
                throw new MergeFailureException("Client", "email", existingClient.getEmail());
            }
        } else {
            throw new ResourceNotFoundException("Client", "ID", String.valueOf(clientID));
        }
    }

}
