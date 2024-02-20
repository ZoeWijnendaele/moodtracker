package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.models.enums.Emotion;

import java.util.List;
import java.util.Optional;

public interface MoodService {

    void addMood(MoodDTO moodDTO);
    Optional<MoodDTO> getMoodById(Long id);
    List<MoodDTO> getAllMoods();
    List<Mood> getAllMoodsByClient(Client client);
    Mood updateMood(MoodDTO moodDTO, Long id);
    Emotion averageEmotion(List<Mood> moods);
    void deleteMood(Long id);


}
