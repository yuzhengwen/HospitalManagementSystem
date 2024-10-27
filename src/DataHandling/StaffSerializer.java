package DataHandling;

import CustomTypes.Gender;
import CustomTypes.Role;
import Model.Staff;
import java.util.StringTokenizer;

/**
 * This class is responsible for serializing and deserializing Staff objects to and from String.
 */
public class StaffSerializer implements ISerializer<Staff> {
    private static final String SEPARATOR = ",";

    @Override
    public String serialize(Staff object) {
        return object.getId() + SEPARATOR +
                object.getName() + SEPARATOR +
                object.getRole() + SEPARATOR +
                object.getGender() + SEPARATOR +
                object.getAge() + SEPARATOR +
                object.getPassword();
    }

    // format: Staff ID,Name,Role,Gender,Age,Password
    @Override
    public Staff deserialize(String data) {
        // get individual 'fields' of the string separated by SEPARATOR
        StringTokenizer star = new StringTokenizer(data, SEPARATOR);    // pass in the string to the string tokenizer using delimiter ","

        String id = star.nextToken().trim();
        String name = star.nextToken().trim();
        Role role = Role.valueOf(star.nextToken().trim().toUpperCase());
        Gender gender = Gender.valueOf(star.nextToken().trim().toUpperCase());
        int age = Integer.parseInt(star.nextToken().trim());
        String password = "";
        if (star.hasMoreTokens()) {
            password = star.nextToken().trim();
        }

        return new Staff(id, password, name, role, gender, age);
    }
}