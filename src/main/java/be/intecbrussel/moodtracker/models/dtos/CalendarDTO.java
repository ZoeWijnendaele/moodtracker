package be.intecbrussel.moodtracker.models.dtos;

import java.time.LocalDateTime;

public class CalendarDTO {

    private Long calendarID;
    private LocalDateTime dateTime;

    public CalendarDTO() { }

    public CalendarDTO(Long calendarID, LocalDateTime dateTime) {
        this.calendarID = calendarID;
        this.dateTime = dateTime;
    }

    public Long getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(Long calendarID) {
        this.calendarID = calendarID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
