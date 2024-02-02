package be.intecbrussel.moodtracker.services.mergers;

import be.intecbrussel.moodtracker.exceptions.MergeFailureException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class ClientMergerService {

    private final ClientRepository clientRepository;

    public ClientMergerService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private <T> void mergeDataFieldIfNotNullAndDifferent(
            String newFieldValue, Function<T, String> getter, Consumer<String> setter, T existingDto) {
        String existingValue = getter.apply(existingDto);

        if (newFieldValue != null && !newFieldValue.isEmpty() && !newFieldValue.equals(existingValue)) {
            setter.accept(newFieldValue);
        }
    }

    public void mergeProfileData(Client existingClient, ProfileDTO profileDTO) {

        try {
            mergeDataFieldIfNotNullAndDifferent(profileDTO.getUserName(),
                    ProfileDTO::getUserName, existingClient::setUserName, profileDTO);
            mergeDataFieldIfNotNullAndDifferent(profileDTO.getEmail(),
                    ProfileDTO::getEmail, existingClient::setEmail, profileDTO);
            mergeDataFieldIfNotNullAndDifferent(profileDTO.getPassword(),
                    ProfileDTO::getPassword, existingClient::setPassword, profileDTO);
            Optional.ofNullable(profileDTO.getAvatar())
                    .filter(avatar -> avatar != Avatar.NO_CHANGE && !avatar.equals(existingClient.getAvatar()))
                    .ifPresent(existingClient::setAvatar);
        } catch (MergeFailureException mergeFailureException) {
            throw new MergeFailureException("Client", "email", existingClient.getEmail());
        }
    }

    public void mergeClientData(Client existingClient, ClientDTO clientDTO) {

        try {
            mergeDataFieldIfNotNullAndDifferent(clientDTO.getUserName(),
                    ClientDTO::getUserName, existingClient::setUserName, clientDTO);
            mergeDataFieldIfNotNullAndDifferent(clientDTO.getEmail(),
                    ClientDTO::getEmail, existingClient::setEmail, clientDTO);
            mergeDataFieldIfNotNullAndDifferent(clientDTO.getPassword(),
                    ClientDTO::getPassword, existingClient::setPassword, clientDTO);
        } catch (MergeFailureException mergeFailureException) {
            throw new MergeFailureException("Client", "email", existingClient.getEmail());
        }
    }

}
