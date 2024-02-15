package be.intecbrussel.moodtracker.models.dtos;

import be.intecbrussel.moodtracker.models.enums.Emotion;

import java.time.LocalDateTime;

public class CalendarDTO {

    private Long calendarID;
    private LocalDateTime date;
    private Emotion averageMood;

    public CalendarDTO() { }

    public CalendarDTO(Long calendarID, LocalDateTime date, Emotion averageMood) {
        this.calendarID = calendarID;
        this.date = date;
        this.averageMood = averageMood;
    }

    public Long getCalendarID() {
        return calendarID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Emotion getAverageMood() {
        return averageMood;
    }

    public void setAverageMood(Emotion averageMood) {
        this.averageMood = averageMood;
    }

}
