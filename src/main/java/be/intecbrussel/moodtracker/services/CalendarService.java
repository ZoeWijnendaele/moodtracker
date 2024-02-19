package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Calendar;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarService {

    void addCalendar(Calendar calendar);
    Optional<Calendar> getCalendarById(Long id);
    List<Calendar> getAllCalendars();
    Calendar getClientCalendarWithAverageEmotion(Client client);

    Calendar getCurrentCalendar(Client client, LocalDateTime date);
    Calendar updateCalendar(Calendar calendar);
    void deleteCalendar(Long id);

}
