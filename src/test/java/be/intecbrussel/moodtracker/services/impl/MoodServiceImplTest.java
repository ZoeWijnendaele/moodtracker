package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.PresentInDatabaseException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.models.enums.Emotion;
import be.intecbrussel.moodtracker.repositories.MoodRepository;
import be.intecbrussel.moodtracker.services.mergers.MoodMergerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MoodServiceImplTest {

    @Mock
    private MoodRepository moodRepository;
    @Mock
    private MoodMergerService moodMergerService;
    @InjectMocks
    private MoodServiceImpl moodService;
    private Mood mood;
    private MoodDTO moodDTO;

    @BeforeEach
    void setUp() {
        mood = new Mood(1L, Emotion.AMUSED, 5, "I am happy");
        moodDTO = new MoodDTO(1L, Emotion.AMUSED, 5, "I am happy");
    }

    @AfterEach
    void tearDown() {
        moodRepository.deleteAll();
        Mockito.reset(moodRepository, moodMergerService);
    }

    @Test
    public void givenMoodDTO_whenAddMood_thenReturnSavedMood() {
        given(moodRepository.findByEmotionAndRatingAndDescription(any(), anyInt(), any()))
                .willReturn(Optional.empty());
        given(moodRepository.save(any(Mood.class))).willReturn(mood);

        moodService.addMood(moodDTO);

        verify(moodRepository, times(1)).findByEmotionAndRatingAndDescription(any(), anyInt(), any());
        verify(moodRepository, times(1)).save(any(Mood.class));
    }

    @Test
    public void givenExistingMood_whenAddMood_thenThrowPresentInDatabaseException() {
        given(moodRepository.findByEmotionAndRatingAndDescription(any(), anyInt(), any()))
                .willReturn(Optional.of(mood));

        assertThrows(PresentInDatabaseException.class, () ->
                moodService.addMood(moodDTO));

        verify(moodRepository, times(1)).findByEmotionAndRatingAndDescription(any(), anyInt(), any());
        verify(moodRepository, times(0)).save(any(Mood.class));
    }

    @Test
    public void givenMoodID_whenGetMoodById_thenReturnMoodDTO() {
        Long moodId = 1L;
        Mood mood = new Mood(moodId, Emotion.ENTHUSIASTIC, 3);
        given(moodRepository.findById(moodId)).willReturn(Optional.of(mood));

        Optional<MoodDTO> moodDTO = moodService.getMoodById(moodId);

        assertThat(moodDTO).isPresent();
        assertThat(moodDTO.get().getMoodID()).isEqualTo(moodId);
        assertThat(moodDTO.get().getEmotion()).isEqualTo(mood.getEmotion());
        assertThat(moodDTO.get().getRating()).isEqualTo(mood.getRating());
    }

    @Test
    public void givenInvalidMoodID_whenGetMoodById_thenThrowResourceNotFoundException() {
        Long invalidId = 1L;
        given(moodRepository.findById(invalidId)).willReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () ->
                        moodService.getMoodById(invalidId));

        assertThat(resourceNotFoundException.getMessage())
                .isEqualTo("Mood with id: '1' not found in database");

        verify(moodRepository).findById(invalidId);
    }

    @Test
    public void givenMoodDTO_whenGetALLMoods_thenReturnListOfMoodObjects() {
        Mood mood1 = new Mood(1L, Emotion.AMUSED, 5, "I am happy");
        Mood mood2 = new Mood(2L, Emotion.ANGRY, 2);
        List<Mood> moods = List.of(mood1, mood2);

        given(moodRepository.findAll()).willReturn(moods);

        List<MoodDTO> result = moodService.getAllMoods();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(moods.size());

        assertThat(result.get(0).getMoodID()).isEqualTo(mood1.getMoodID());
        assertThat(result.get(0).getEmotion()).isEqualTo(mood1.getEmotion());
        assertThat(result.get(0).getRating()).isEqualTo(mood1.getRating());
        assertThat(result.get(0).getDescription()).isEqualTo(mood1.getDescription());

        assertThat(result.get(1).getMoodID()).isEqualTo(mood2.getMoodID());
        assertThat(result.get(1).getEmotion()).isEqualTo(mood2.getEmotion());
        assertThat(result.get(1).getRating()).isEqualTo(mood2.getRating());

        verify(moodRepository, times(1)).findAll();
    }

    @Test
    public void givenMoodDTO_whenUpdateMood_thenReturnUpdatedMood() {
    }
}