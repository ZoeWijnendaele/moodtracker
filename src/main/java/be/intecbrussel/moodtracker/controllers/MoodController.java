package be.intecbrussel.moodtracker.controllers;

import be.intecbrussel.moodtracker.exceptions.ResourceNotFoundException;
import be.intecbrussel.moodtracker.models.Mood;
import be.intecbrussel.moodtracker.models.dtos.MoodDTO;
import be.intecbrussel.moodtracker.services.MoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mood> addMood(@RequestBody MoodDTO moodDTO) {

        try {
            moodService.addMood(moodDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MoodDTO> getMoodById(@PathVariable("id") Long id) {
        Optional<MoodDTO> mood = moodService.getMoodById(id);

        return ResponseEntity.ok(mood.orElseThrow(
                () -> new ResourceNotFoundException("Mood", "id", String.valueOf(id))));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<MoodDTO>> getAllMoods() {
        return ResponseEntity.ok(moodService.getAllMoods());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Mood> updateMood(@RequestBody MoodDTO moodDTO, @PathVariable("id") Long id) {
        return ResponseEntity.ok(moodService.updateMood(moodDTO, id));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMood(@PathVariable("id") Long id) {
        moodService.deleteMood(id);
    }

}
