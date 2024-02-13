package be.intecbrussel.moodtracker.repositories;

import be.intecbrussel.moodtracker.models.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodRepository extends JpaRepository<Mood, Long> {
}
