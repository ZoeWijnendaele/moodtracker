package be.intecbrussel.moodtracker.models.mappers;

import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;

public class MoodMapper {

    public static MoodDTO mapMoodToMoodDTO(Mood mood) {

        return new MoodDTO(
                mood.getMoodID(),
                mood.getEmotion(),
                mood.getRating(),
                mood.getDescription(),
                mood.getDateTime(),
                mood.getClient());
    }

    public static Mood mapMoodDTOToMood(MoodDTO moodDTO) {

        return new Mood(
                moodDTO.getMoodID(),
                moodDTO.getEmotion(),
                moodDTO.getRating(),
                moodDTO.getDescription(),
                moodDTO.getDateTime(),
                moodDTO.getClient());
    }

}
