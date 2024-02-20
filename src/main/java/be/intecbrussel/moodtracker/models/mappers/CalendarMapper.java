package be.intecbrussel.moodtracker.models.mappers;

import be.intecbrussel.moodtracker.models.Calendar;
import be.intecbrussel.moodtracker.models.dtos.CalendarDTO;

public class CalendarMapper {

    public static CalendarDTO mapCalendarToCalendarDTO(Calendar calendar) {

        return new CalendarDTO(
                calendar.getCalendarID(),
                calendar.getDateTime(),
                calendar.getAverageEmotion(),
                calendar.getClient().getClientID());
    }

    public static Calendar mapCalendarDTOToCalendar(CalendarDTO calendarDTO) {

        return new Calendar(
                calendarDTO.getCalendarID(),
                calendarDTO.getDateTime(),
                calendarDTO.getAverageEmotion());
    }

}
