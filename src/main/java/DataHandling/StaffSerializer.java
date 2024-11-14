package DataHandling;

import CustomTypes.Gender;
import CustomTypes.Role;
import Encryption.AESEncryption;
import Model.Staff;

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
                AESEncryption.encrypt(object.getPassword(), "secret", "salt");
    }

    // format: Staff ID,Name,Role,Gender,Age,Password
    @Override
    public Staff deserialize(String data) {
        String[] parts = data.split(SEPARATOR, -1);

        String id = parts[0].trim();
        String name = parts[1].trim();
        Role role = Role.valueOf(parts[2].trim().toUpperCase());
        Gender gender = Gender.valueOf(parts[3].trim().toUpperCase());
        int age = Integer.parseInt(parts[4].trim());
        String password = ""; // default password is empty
        if (parts.length > 5) {
            String encryptedPassword = parts[5].trim();
            password = AESEncryption.decrypt(encryptedPassword, "secret", "salt");
        }

        return new Staff(id, password, name, role, gender, age);
    }
}