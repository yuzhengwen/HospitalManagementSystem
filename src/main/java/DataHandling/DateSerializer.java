package DataHandling;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for serializing and deserializing LocalDate objects to and from String.
 */
public class DateSerializer implements ISerializer<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    @Override
    public String serialize(LocalDate object) {
        return object.format(formatter);
    }

    @Override
    public LocalDate deserialize(String data) {
        try {
            return LocalDate.parse(data, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
