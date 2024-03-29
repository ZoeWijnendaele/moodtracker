package be.intecbrussel.moodtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MoodtrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodtrackerApplication.class, args);
    }

}
