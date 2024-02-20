package be.intecbrussel.moodtracker.repositories;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.enums.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    Optional<Mood> findByEmotionAndRatingAndDescription(Emotion emotion, int rating, String description);
    List<Mood> findMoodByClient(Client client);

}
