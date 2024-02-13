package be.intecbrussel.moodtracker.services.mergers;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class FieldMergerService {

    public <T, U> void mergeFieldIfNotNullAndDifferent(
            U newFieldValue, Function<T, U> getter,
            Consumer<U> setter, T existingDto) {
        U existingValue = getter.apply(existingDto);

        if (newFieldValue != null && !newFieldValue.equals(existingValue) &&
                (!(newFieldValue instanceof String) ||
                        !((String)newFieldValue).isEmpty())) {
            setter.accept(newFieldValue);
        }
    }

}
