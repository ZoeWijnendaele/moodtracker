package be.intecbrussel.moodtracker.exceptions;

import org.springframework.http.HttpStatus;

public class MergeFailureException extends RuntimeException {

    private final HttpStatus status = HttpStatus.CONFLICT;

    public MergeFailureException(String dataType, String identifierName, String identifierValue) {
        super("%s with %s: '%s' not able to merge".formatted(dataType, identifierName, identifierValue));    }

    public MergeFailureException(String calendar, Long id, Long calendarId) {
        super("%s with %s: '%s' not able to merge".formatted(calendar, id, calendarId));
    }

    public HttpStatus getStatus() {
        return status;
    }

}
