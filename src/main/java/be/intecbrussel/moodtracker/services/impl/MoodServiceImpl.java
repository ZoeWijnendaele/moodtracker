package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.MergeFailureException;
import be.intecbrussel.moodtracker.exceptions.PresentInDatabaseException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.models.mappers.MoodMapper;
import be.intecbrussel.moodtracker.repositories.MoodRepository;
import be.intecbrussel.moodtracker.services.MoodService;
import be.intecbrussel.moodtracker.services.mergers.MoodMergerService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodServiceImpl implements MoodService {

    private final MoodRepository moodRepository;
    private final MoodMergerService moodMergerService;

    public MoodServiceImpl(MoodRepository moodRepository,
                           MoodMergerService moodMergerService) {
        this.moodRepository = moodRepository;
        this.moodMergerService = moodMergerService;
    }

    @Override
    public void addMood(MoodDTO moodDTO) {

        if (moodDTO.getEmotion() == null || moodDTO.getRating() == 0) {
            throw new IllegalArgumentException("Emotion and Rating cannot be empty");
        }

        if (moodRepository.findByEmotionAndRatingAndDescription(
                moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription()).isPresent()) {
            throw new PresentInDatabaseException("Mood", "Emotion, Rating, and Description",
                    String.format("(%s, %d, %s)", moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription()));
        }

        Mood mood = MoodMapper.mapMoodDTOToMood(moodDTO);
        moodRepository.save(mood);
    }

    @Override
    public Optional<MoodDTO> getMoodById(Long id) {

        return moodRepository.findById(id)
                .map(MoodMapper::mapMoodToMoodDTO)
                .map(Optional::of)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mood", "id", String.valueOf(id)));
    }

    @Override
    public List<MoodDTO> getAllMoods() {
        List<Mood> moods = moodRepository.findAll();

        return moods.stream()
                .map(MoodMapper::mapMoodToMoodDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Mood updateMood(MoodDTO moodDTO, Long id) {

        try {
            Mood mood = moodRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mood", "id", String.valueOf(id)));

            moodMergerService.mergeMood(mood.getMoodID(), moodDTO);

            return moodRepository.save(mood);
        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new ResourceNotFoundException("Mood", "id", String.valueOf(id));
        } catch (MergeFailureException mergeFailureException) {
            throw new MergeFailureException("Mood", "id", String.valueOf(id));
        }
    }

    //TODO: when deleting a mood, update client and calendar
    @Override
    public void deleteMood(Long id) {

        try {
            moodRepository.deleteById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new ResourceNotFoundException("Mood", "id", String.valueOf(id));
        }
    }

}
