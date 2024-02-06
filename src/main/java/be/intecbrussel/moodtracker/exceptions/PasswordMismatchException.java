package be.intecbrussel.moodtracker.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends RuntimeException {

    private final HttpStatus status = HttpStatus.CONFLICT;

    public PasswordMismatchException(String dataType, String identifierName, String identifierValue) {
        super("%s with %s: '%s' not a valid password".formatted(dataType, identifierName, identifierValue));    }

    public PasswordMismatchException(String calendar, Long id, Long calendarId) {
        super("%s with %s: '%s' not a valid password".formatted(calendar, id, calendarId));
    }

    public HttpStatus getStatus() {
        return status;
    }

}
