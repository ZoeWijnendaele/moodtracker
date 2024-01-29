package be.intecbrussel.moodtracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Mood {

    @Id
    private Long moodID;

}
