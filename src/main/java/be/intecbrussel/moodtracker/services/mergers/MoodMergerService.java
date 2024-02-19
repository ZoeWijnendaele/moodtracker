package be.intecbrussel.moodtracker.services.mergers;

import be.intecbrussel.moodtracker.exceptions.MergeFailureException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.repositories.MoodRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MoodMergerService {

    private final MoodRepository moodRepository;
    private final FieldMergerService fieldMergerService;

    public MoodMergerService(MoodRepository moodRepository, FieldMergerService fieldMergerService) {
        this.moodRepository = moodRepository;
        this.fieldMergerService = fieldMergerService;
    }

    public void mergeMood(Long moodID, MoodDTO moodDTO) {
        Optional<Mood> optionalMood = moodRepository.findById(moodID);

        if (optionalMood.isPresent()) {
            Mood existingMood = optionalMood.get();

            try {
                fieldMergerService.mergeFieldIfNotNullAndDifferent(
                        moodDTO.getEmotion(),
                        Mood::getEmotion,
                        existingMood::setEmotion,
                        moodDTO, existingMood);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(
                        moodDTO.getRating(),
                        Mood::getRating,
                        existingMood::setRating,
                        moodDTO, existingMood);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(
                        moodDTO.getDescription(),
                        Mood::getDescription,
                        existingMood::setDescription,
                        moodDTO, existingMood);
            } catch (MergeFailureException mergeFailureException) {
                throw new MergeFailureException("Mood", "ID", String.valueOf(existingMood.getMoodID()));
            }
        } else {
            throw new ResourceNotFoundException("Mood", "ID", String.valueOf(moodID));
        }

    }
}
