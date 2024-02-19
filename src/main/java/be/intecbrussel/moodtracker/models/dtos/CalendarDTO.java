package be.intecbrussel.moodtracker.models.dtos;

import be.intecbrussel.moodtracker.models.enums.Emotion;

import java.time.LocalDateTime;

public class CalendarDTO {

    private Long calendarID;
    private LocalDateTime dateTime;
    private Emotion averageEmotion;

    public CalendarDTO() { }

    public CalendarDTO(Long calendarID, LocalDateTime dateTime, Emotion averageEmotion) {
        this.calendarID = calendarID;
        this.dateTime = dateTime;
        this.averageEmotion = averageEmotion;
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

    public Emotion getAverageEmotion() {
        return averageEmotion;
    }

    public void setAverageEmotion(Emotion averageEmotion) {
        this.averageEmotion = averageEmotion;
    }

}
