package be.intecbrussel.moodtracker.models.mappers;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;

public class ClientMapper {

    public static ClientDTO mapClientToClientDTO(Client client) {

        return new ClientDTO(
                client.getClientID(),
                client.getUserName(),
                client.getEmail(),
                client.getPassword());
    }

    public static Client mapClientDTOToClient(ClientDTO clientDTO) {

        return new Client(
                clientDTO.getClientID(),
                clientDTO.getUserName(),
                clientDTO.getEmail(),
                clientDTO.getPassword());
    }

}
