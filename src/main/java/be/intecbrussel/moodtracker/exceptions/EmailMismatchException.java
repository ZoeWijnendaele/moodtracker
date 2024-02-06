package be.intecbrussel.moodtracker.exceptions;

import org.springframework.http.HttpStatus;

public class EmailMismatchException extends RuntimeException {

    private final HttpStatus status = HttpStatus.CONFLICT;

    public EmailMismatchException(String dataType, String identifierName, String identifierValue) {
        super("%s with %s: '%s' not a valid email".formatted(dataType, identifierName, identifierValue));    }

    public EmailMismatchException(String calendar, Long id, Long calendarId) {
        super("%s with %s: '%s' not a valid email".formatted(calendar, id, calendarId));
    }

    public HttpStatus getStatus() {
        return status;
    }

}
