package be.intecbrussel.moodtracker.repositories;

import be.intecbrussel.moodtracker.models.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
