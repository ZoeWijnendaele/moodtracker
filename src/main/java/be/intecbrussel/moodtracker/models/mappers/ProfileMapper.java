package be.intecbrussel.moodtracker.models.mappers;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ProfileDTO;
import org.springframework.context.annotation.Profile;

public class ProfileMapper {

    public static ProfileDTO mapClientToProfileDTO(Client client) {

        return new ProfileDTO(
                client.getClientID(),
                client.getUserName(),
                client.getEmail(),
                client.getPassword(),
                client.getBirthday(),
                client.getAvatar());
    }

    public static Client mapProfileDTOToProfile(ProfileDTO profileDTO) {

        return new Client(
                profileDTO.getClientID(),
                profileDTO.getUserName(),
                profileDTO.getEmail(),
                profileDTO.getPassword(),
                profileDTO.getBirthday(),
                profileDTO.getAvatar());
    }
}
