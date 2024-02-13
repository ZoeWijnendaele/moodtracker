package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.repositories.MoodRepository;
import be.intecbrussel.moodtracker.services.MoodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoodServiceImpl implements MoodService {

    private final MoodRepository moodRepository;

    @Override
    public void addMood(MoodDTO moodDTO) {

    }

    @Override
    public Optional<MoodDTO> getMoodById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<MoodDTO> getAllMoods() {
        return null;
    }

    @Override
    public Mood updateMood(MoodDTO moodDTO, Long id) {
        return null;
    }

    @Override
    public void deleteMood(Long id) {

    }
}
