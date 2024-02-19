package be.intecbrussel.moodtracker.models.dtos;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MoodDTO {

    private Long moodID;
    @Enumerated(EnumType.STRING)
    private Emotion emotion;
    private int rating;
    private String description;
    private LocalDateTime dateTime;
    private Client client;

    public MoodDTO() { }

    public MoodDTO(Long moodID, Emotion emotion, int rating, String description, LocalDateTime dateTime, Client client) {
        this.moodID = moodID;
        this.emotion = emotion;
        this.rating = rating;
        this.description = description;
        this.dateTime = dateTime;
        this.client = client;
    }

    public Long getMoodID() {
        return moodID;
    }

    public void setMoodID(Long moodID) {
        this.moodID = moodID;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
