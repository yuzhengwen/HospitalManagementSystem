package DataHandling;

import java.util.StringTokenizer;

public class DoctorSerializer implements ISerializer<Doctor>{
    @Override
    public String serialize(Doctor object) {
        return object.getId() + SEPARATOR + object.getPassword();
    }

    @Override
    public Doctor deserialize(String data) {
        StringTokenizer star = new StringTokenizer(data, SEPARATOR);
        String id = star.nextToken().trim();
        String password = star.nextToken().trim();
        return new Doctor(id, password);
    }
}
