package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.MergeFailureException;
import be.intecbrussel.moodtracker.exceptions.PresentInDatabaseException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.ClientDTO;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.models.enums.Emotion;
import be.intecbrussel.moodtracker.models.mappers.ClientMapper;
import be.intecbrussel.moodtracker.models.mappers.MoodMapper;
import be.intecbrussel.moodtracker.repositories.MoodRepository;
import be.intecbrussel.moodtracker.services.MoodService;
import be.intecbrussel.moodtracker.services.mergers.MoodMergerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodServiceImpl implements MoodService {

    private final MoodRepository moodRepository;
    private final MoodMergerService moodMergerService;
    private final ClientServiceImpl clientService;

    public MoodServiceImpl(MoodRepository moodRepository,
                           MoodMergerService moodMergerService,
                           @Lazy ClientServiceImpl clientService) {
        this.moodRepository = moodRepository;
        this.moodMergerService = moodMergerService;
        this.clientService = clientService;
    }

    @Override
    public void addMood(MoodDTO moodDTO) {
        if (moodDTO.getEmotion() == null || moodDTO.getRating() == 0) {
            throw new IllegalArgumentException("Emotion and Rating cannot be empty");
        }

        Optional<Client> currentClient = clientService.getClientByIdForMood(moodDTO.getClientID());

        if (currentClient.isEmpty()) {
            throw new ResourceNotFoundException("Client", "id", String.valueOf(moodDTO.getClientID()));
        }

        if (moodRepository.findByEmotionAndRatingAndDescription(
                moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription()).isPresent()) {
            throw new PresentInDatabaseException("Mood", "Emotion, Rating, and Description",
                    String.format("(%s, %d, %s)", moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription()));
        }

        Mood mood = new Mood(moodDTO.getMoodID(), moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription(), LocalDateTime.now(), currentClient.get());
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
    public List<Mood> getAllMoodsByClient(Client client) {
        return moodRepository.findMoodByClient(client);
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

    @Override
    public Emotion averageEmotion(List<Mood> moods) {

        if (moods.isEmpty()) {
            throw new ResourceNotFoundException("Mood", "id", String.valueOf(moods));
        }

        Map<Emotion, Long> emotionCount = moods.stream()
                .collect(Collectors.groupingBy(Mood::getEmotion, Collectors.counting()));

        Optional<Map.Entry<Emotion, Long>> maxEmotion = emotionCount.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return maxEmotion.map(Map.Entry::getKey).orElse(null);
    }

    //TODO: when deleting a mood, update calendar
    @Override
    public void deleteMood(Long id) {

        try {
            moodRepository.deleteById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new ResourceNotFoundException("Mood", "id", String.valueOf(id));
        }
    }

}
