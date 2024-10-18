package DataHandling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is responsible for serializing and deserializing Date objects to and from String.
 */
public class DateSerializer implements ISerializer<Date> {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String serialize(Date object) {
        return formatter.format(object);
    }

    @Override
    public Date deserialize(String data) {
        try {
            return formatter.parse(data);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
