package DataHandling;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSerializer implements ISerializer<LocalTime> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String serialize(LocalTime object) {
        return object.format(formatter);
    }

    @Override
    public LocalTime deserialize(String data) {
        try {
            return LocalTime.parse(data, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
