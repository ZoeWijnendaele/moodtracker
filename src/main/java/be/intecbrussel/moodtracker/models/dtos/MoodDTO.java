package be.intecbrussel.moodtracker.models.dtos;

import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;

public class MoodDTO {

    private Long moodID;
    @NotEmpty(message = "Please provide an emotion")
    @Enumerated(EnumType.STRING)
    private Emotion emotion;
    @NotEmpty(message = "Please provide a rating")
    private int rating;
    private String description;

    public MoodDTO() { }

    public MoodDTO(Long moodID, Emotion emotion, int rating, String description) {
        this.moodID = moodID;
        this.emotion = emotion;
        this.rating = rating;
        this.description = description;
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

}
