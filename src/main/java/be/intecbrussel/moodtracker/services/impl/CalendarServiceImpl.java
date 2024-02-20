package be.intecbrussel.moodtracker.services.impl;

import be.intecbrussel.moodtracker.exceptions.PresentInDatabaseException;
import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Calendar;
import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.CalendarDTO;
import be.intecbrussel.moodtracker.models.enums.Emotion;
import be.intecbrussel.moodtracker.models.mappers.CalendarMapper;
import be.intecbrussel.moodtracker.repositories.CalendarRepository;
import be.intecbrussel.moodtracker.services.CalendarService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final MoodServiceImpl moodService;
    private final ClientServiceImpl clientService;

    public CalendarServiceImpl(CalendarRepository calendarRepository, MoodServiceImpl moodService, ClientServiceImpl clientService) {
        this.calendarRepository = calendarRepository;
        this.moodService = moodService;
        this.clientService = clientService;
    }

    @Override
    public void addCalendar(CalendarDTO calendarDTO) {
        Long calendarID = calendarDTO.getCalendarID();

        if (calendarRepository.existsById(calendarID)) {
            throw new PresentInDatabaseException("Calendar", "id", String.valueOf(calendarID));
        }

        Calendar calendar = CalendarMapper.mapCalendarDTOToCalendar(calendarDTO);
        calendarRepository.save(calendar);
    }

    @Override
    public Optional<CalendarDTO> getCalendarById(Long id) {

        return calendarRepository.findById(id)
                .map(CalendarMapper::mapCalendarToCalendarDTO)
                .map(Optional::of)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Calendar", "id", String.valueOf(id)));
    }

    @Override
    public List<CalendarDTO> getAllCalendars() {
        List<Calendar> calendars = calendarRepository.findAll();

        return calendars.stream()
                .map(CalendarMapper::mapCalendarToCalendarDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CalendarDTO getClientCalendarWithAverageEmotion(Client client, LocalDateTime date) {

        try {
            List<Mood> clientMoods = moodService.getAllMoodsByClient(client);
            Emotion averageEmotion = moodService.averageEmotion(clientMoods);

            CalendarDTO calendarDTO = new CalendarDTO();
            calendarDTO.setDateTime(date);
            calendarDTO.setAverageEmotion(averageEmotion);
            calendarDTO.setClientID(client.getClientID());

            return calendarDTO;
        } catch (ResourceNotFoundException clientNotFoundException) {
            throw new ResourceNotFoundException("Client", "id", String.valueOf(client.getClientID()));
        }
    }

    @Override
    public Calendar getCurrentCalendar(Client client, LocalDateTime date) {
        return null;
    }

    @Override
    public Calendar updateCalendar(Calendar calendar) {
        return null;
    }

    @Override
    public void deleteCalendar(Long id) {

    }
}
