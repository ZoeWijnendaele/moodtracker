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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodServiceImpl implements MoodService {

    private final MoodRepository moodRepository;
    private final MoodMergerService moodMergerService;
    private final ClientServiceImpl clientService;

    public MoodServiceImpl(MoodRepository moodRepository,
                           MoodMergerService moodMergerService,
                           ClientServiceImpl clientService) {
        this.moodRepository = moodRepository;
        this.moodMergerService = moodMergerService;
        this.clientService = clientService;
    }

    @Override
    public void addMood(MoodDTO moodDTO) {

        if (moodDTO.getEmotion() == null || moodDTO.getRating() == 0) {
            throw new IllegalArgumentException("Emotion and Rating cannot be empty");
        }

        ClientDTO currentClientDTO = clientService.getCurrentClient();
        Client client = ClientMapper.mapClientDTOToClient(currentClientDTO);

        if (moodRepository.findByEmotionAndRatingAndDescription(
                moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription()).isPresent()) {
            throw new PresentInDatabaseException("Mood", "Emotion, Rating, and Description",
                    String.format("(%s, %d, %s)", moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription()));
        }

        Mood mood = new Mood(moodDTO.getMoodID(), moodDTO.getEmotion(), moodDTO.getRating(), moodDTO.getDescription(), LocalDateTime.now(), client);
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
    public List<Mood> getMoodsForDate(Client client, LocalDateTime dateTime) {
        if (client == null || dateTime == null) {
            throw new IllegalArgumentException("Client or date cannot be empty");
        }

        List<Mood> moods = moodRepository.findByClientAndDateTime(client, dateTime);

        if (moods.isEmpty()) {
            throw new ResourceNotFoundException("Moods", "Client ID",
                    client.getClientID(), dateTime.toLocalDate().toEpochDay());
        }

        return moods;
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
        return null;
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
