package DataHandling;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import Encryption.AESEncryption;
import Model.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for serializing and deserializing Patient objects to and from String.
 */
public class PatientSerializer implements ISerializer<Patient> {
    private final DateSerializer dateSerializer = new DateSerializer();
    private final ContactInfoSerializer contactInfoSerializer = new ContactInfoSerializer();

    @Override
    public String serialize(Patient object) {
        // if list is empty, join will return an empty string
        String serializedDiagnosisHistory = StringUtils.addQuotes(String.join(",", object.getDiagnosisHistory()));
        String serializedTreatmentHistory = StringUtils.addQuotes(String.join(",", object.getTreatmentHistory()));
        String serializedPrescriptionHistory = StringUtils.addQuotes(String.join(",", object.getPrescriptionHistory()));
        return object.getId() + SEPARATOR +
                object.getName() + SEPARATOR +
                dateSerializer.serialize(object.getDob()) + SEPARATOR +
                object.getGender() + SEPARATOR +
                object.getBloodType() + SEPARATOR +
                contactInfoSerializer.serialize(object.getContactInfo()) + SEPARATOR +
                serializedDiagnosisHistory + SEPARATOR +
                serializedTreatmentHistory + SEPARATOR +
                serializedPrescriptionHistory + SEPARATOR +
                AESEncryption.encrypt(object.getPassword(), "secret", "salt");
    }

    // format: Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information
    @Override
    public Patient deserialize(String data) {
        // get individual 'fields' of the string separated by SEPARATOR
        //String[] splitData = data.split(SEPARATOR, -1);
        String[] splitData = StringUtils.parseLine(data);
        assert splitData.length >= 9 : "Invalid data format";
        String id = splitData[0].trim();
        String name = splitData[1].trim();
        LocalDate dob = dateSerializer.deserialize(splitData[2].trim());
        Gender gender = Gender.valueOf(splitData[3].trim().toUpperCase());
        String bloodType = splitData[4].trim();
        ContactInfo contactInfo = contactInfoSerializer.deserialize(splitData[5].trim());
        List<String> diagnosisHistory = new ArrayList<>();
        List<String> treatmentHistory = new ArrayList<>();
        List<String> prescriptionHistory = new ArrayList<>();
        if (!splitData[6].trim().isEmpty()) {
            diagnosisHistory.addAll(Arrays.stream(splitData[6].trim().split(",")).toList());
        }
        if (!splitData[7].trim().isEmpty()) {
            treatmentHistory.addAll(Arrays.stream(splitData[7].trim().split(",")).toList());
        }
        if (!splitData[8].trim().isEmpty()) {
            prescriptionHistory.addAll(Arrays.stream(splitData[8].trim().split(",")).toList());
        }
        String password = ""; // default password is empty
        if (splitData.length > 9) {
            String encryptedPassword = splitData[9].trim();
            password = AESEncryption.decrypt(encryptedPassword, "secret", "salt");
        }

        Patient p = new Patient(id, password, name, dob, gender);
        p.setBloodType(bloodType);
        p.setContactInfo(contactInfo.phoneNumber, contactInfo.email);
        if (!diagnosisHistory.isEmpty()) {
            p.addDiagnoses(diagnosisHistory);
        }
        if (!treatmentHistory.isEmpty()) {
            p.addTreatments(treatmentHistory);
        }
        if (!prescriptionHistory.isEmpty()) {
            p.addPrescriptions(prescriptionHistory);
        }

        return p;
    }
}
