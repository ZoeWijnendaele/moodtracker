package be.intecbrussel.moodtracker.services;

import be.intecbrussel.moodtracker.models.Calendar;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.dtos.CalendarDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarService {

    void addCalendar(CalendarDTO calendarDTO);
    Optional<CalendarDTO> getCalendarById(Long id);
    List<CalendarDTO> getAllCalendars();
    CalendarDTO getClientCalendarWithAverageEmotion(Client client, LocalDateTime date);

    Calendar getCurrentCalendar(Client client, LocalDateTime date);
    Calendar updateCalendar(Calendar calendar);
    void deleteCalendar(Long id);

}
