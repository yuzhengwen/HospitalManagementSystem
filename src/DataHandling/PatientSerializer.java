package DataHandling;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import Model.Patient;
import java.util.Date;
import java.util.StringTokenizer;

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
                object.getPassword();
    }

    // format: Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information
    @Override
    public Patient deserialize(String data) {
        // get individual 'fields' of the string separated by SEPARATOR
        StringTokenizer star = new StringTokenizer(data, SEPARATOR);    // pass in the string to the string tokenizer using delimiter ","

        String id = star.nextToken().trim();
        String name = star.nextToken().trim();
        Date dob = dateSerializer.deserialize(star.nextToken().trim());
        Gender gender = Gender.valueOf(star.nextToken().trim().toUpperCase());
        String bloodType = star.nextToken().trim();
        ContactInfo contactInfo = contactInfoSerializer.deserialize(star.nextToken().trim());
        String password = ""; // default password is empty
        if (star.hasMoreTokens()) {
            password = star.nextToken().trim();
        }

        Patient p = new Patient(id, password, name, dob, bloodType, gender);
        p.setContactInfo(contactInfo);
        return p;
    }
}
