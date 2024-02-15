package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarID;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "average_mood")
    private Emotion averageMood;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    protected Calendar() { }

    public Calendar(Long calendarID, LocalDateTime date, Emotion averageMood, Client client, Mood mood) {
        this.calendarID = calendarID;
        this.date = date;
        this.averageMood = averageMood;
        this.client = client;
        this.mood = mood;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

}
