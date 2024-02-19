package be.intecbrussel.moodtracker.repositories;

import be.intecbrussel.moodtracker.models.Calendar;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
