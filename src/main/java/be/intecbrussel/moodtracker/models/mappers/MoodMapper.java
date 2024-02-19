package be.intecbrussel.moodtracker.models.mappers;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import jakarta.persistence.EntityNotFoundException;

public class MoodMapper {

    public static MoodDTO mapMoodToMoodDTO(Mood mood) {

        return new MoodDTO(
                mood.getMoodID(),
                mood.getEmotion(),
                mood.getRating(),
                mood.getDescription(),
                mood.getDateTime(),
                mood.getClient() != null ? mood.getClient().getClientID() : null);
    }

    public static Mood mapMoodDTOToMood(MoodDTO moodDTO, Client client) {

        return new Mood(
                moodDTO.getMoodID(),
                moodDTO.getEmotion(),
                moodDTO.getRating(),
                moodDTO.getDescription(),
                moodDTO.getDateTime(),
                client);
    }

}
