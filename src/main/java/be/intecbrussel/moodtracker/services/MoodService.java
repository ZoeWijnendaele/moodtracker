package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;

import java.util.List;
import java.util.Optional;

public interface MoodService {

    void addMood(MoodDTO moodDTO);
    Optional<MoodDTO> getMoodById(Long id);
    List<MoodDTO> getAllMoods();
    Mood updateMood(MoodDTO moodDTO, Long id);
    void deleteMood(Long id);

}
