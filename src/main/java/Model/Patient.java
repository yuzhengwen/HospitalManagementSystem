package Model;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import CustomTypes.Role;

import java.time.LocalDate;
import java.util.Objects;

public class Patient extends User {
    private LocalDate dob;
    private ContactInfo contactInfo = new ContactInfo();
    private String bloodType;
    // to add blood type, past diagnosis/treatment

    public Patient(String id, String password, String name, LocalDate dob, String bloodType, Gender gender) {
        super(id, password, Role.PATIENT, name, gender);
        this.dob = dob;
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDate getDob() {
        return dob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(name, patient.name) && role == patient.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role);
    }

    @Override
    public String toString() {
        return name;
    }
}
