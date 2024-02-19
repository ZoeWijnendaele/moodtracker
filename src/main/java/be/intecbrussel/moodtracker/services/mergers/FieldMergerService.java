package be.intecbrussel.moodtracker.services.mergers;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class FieldMergerService {

    public <T, U, O> void mergeFieldIfNotNullAndDifferent(
            U newFieldValue,
            Function<O, U> getter,
            Consumer<U> setter,
            T existingDto,
            O existingObject
    ) {

        U existingValue = getter.apply(existingObject);

        if (
                newFieldValue != null &&
                !newFieldValue.equals(existingValue) &&
                (!(newFieldValue instanceof String) || !((String)newFieldValue).isEmpty())
        ) {
            setter.accept(newFieldValue);
        }
    }

}
