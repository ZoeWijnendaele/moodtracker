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
                fieldMergerService.mergeFieldIfNotNullAndDifferent(moodDTO.getEmotion(),
                        MoodDTO::getEmotion, existingMood::setEmotion, moodDTO);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(moodDTO.getRating(),
                        MoodDTO::getRating, existingMood::setRating, moodDTO);
                fieldMergerService.mergeFieldIfNotNullAndDifferent(moodDTO.getDescription(),
                        MoodDTO::getDescription, existingMood::setDescription, moodDTO);
            } catch (MergeFailureException mergeFailureException) {
                throw new MergeFailureException("Mood", "ID", String.valueOf(existingMood.getMoodID()));
            }
        } else {
            throw new ResourceNotFoundException("Mood", "ID", String.valueOf(moodID));
        }

    }
}
