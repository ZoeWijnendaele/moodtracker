package be.intecbrussel.moodtracker.exceptions;

import org.springframework.http.HttpStatus;

public class EmailValidateFailureException extends RuntimeException {

    private final HttpStatus status = HttpStatus.CONFLICT;

    public EmailValidateFailureException(String dataType, String identifierName, String identifierValue) {
        super("%s with %s: '%s' not a valid email".formatted(dataType, identifierName, identifierValue));    }

    public EmailValidateFailureException(String calendar, Long id, Long calendarId) {
        super("%s with %s: '%s' not a valid email".formatted(calendar, id, calendarId));
    }

    public HttpStatus getStatus() {
        return status;
    }

}
