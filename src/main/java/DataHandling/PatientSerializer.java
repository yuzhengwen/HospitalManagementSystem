package DataHandling;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import Encryption.AESEncryption;
import Model.Patient;

import java.time.LocalDate;

/**
 * This class is responsible for serializing and deserializing Patient objects to and from String.
 */
public class PatientSerializer implements ISerializer<Patient> {
    private final DateSerializer dateSerializer = new DateSerializer();
    private final ContactInfoSerializer contactInfoSerializer = new ContactInfoSerializer();

    @Override
    public String serialize(Patient object) {
        return object.getId() + SEPARATOR +
                object.getName() + SEPARATOR +
                dateSerializer.serialize(object.getDob()) + SEPARATOR +
                object.getGender() + SEPARATOR +
                object.getBloodType() + SEPARATOR +
                contactInfoSerializer.serialize(object.getContactInfo()) + SEPARATOR +
                AESEncryption.encrypt(object.getPassword(), "secret", "salt");
    }

    // format: Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information
    @Override
    public Patient deserialize(String data) {
        // get individual 'fields' of the string separated by SEPARATOR
        String[] splitData = data.split(SEPARATOR, -1);
        String id = splitData[0].trim();
        String name = splitData[1].trim();
        LocalDate dob = dateSerializer.deserialize(splitData[2].trim());
        Gender gender = Gender.valueOf(splitData[3].trim().toUpperCase());
        String bloodType = splitData[4].trim();
        ContactInfo contactInfo = contactInfoSerializer.deserialize(splitData[5].trim());
        String password = ""; // default password is empty
        if (splitData.length > 6) {
            String encryptedPassword = splitData[6].trim();
            password = AESEncryption.decrypt(encryptedPassword, "secret", "salt");
        }

        Patient p = new Patient(id, password, name, dob, bloodType, gender);
        p.setContactInfo(contactInfo);
        return p;
    }
}
