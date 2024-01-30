package be.intecbrussel.moodtracker.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String dataType, String identifierName, String identifierValue) {
        super("%s with %s: '%s' not found in database".formatted(dataType, identifierName, identifierValue));
    }

    public ResourceNotFoundException(String calendar, Long id, Long calendarId) {
        super("%s with %s: '%s' not found in database".formatted(calendar, id, calendarId));
    }

    public HttpStatus getStatus() {
        return status;
    }

}
