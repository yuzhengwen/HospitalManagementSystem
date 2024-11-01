package DataHandling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * This class is responsible for serializing and deserializing LocalDate objects to and from String.
 */
public class DateSerializer implements ISerializer<LocalDate> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


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
