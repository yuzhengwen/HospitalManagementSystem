package Model;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import CustomTypes.Role;
import Singletons.AppointmentFilter;
import Singletons.AppointmentManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Patient extends User {
    private LocalDate dob;
    private ContactInfo contactInfo = new ContactInfo();
    private String bloodType;
    private final AppointmentFilter pastAppointmentsFilter;

    public Patient(String id, String password, String name, LocalDate dob, String bloodType, Gender gender) {
        super(id, password, Role.PATIENT, name, gender);
        this.dob = dob;
        this.bloodType = bloodType;
        pastAppointmentsFilter = new AppointmentFilter().filterByPatient(id).filterByStatus(Appointment.Status.COMPLETED);
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setBloodType(String bloodType) {
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
        return Objects.hash(id, name, role);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getMedicalRecord() {
        return "Medical Record: \n" +
                "Patient ID: " + id + '\n' +
                "Name: " + name + '\n' +
                "Date of Birth: " + dob + '\n' +
                "Gender: " + gender + '\n' +
                "Blood Type: " + bloodType + '\n' +
                "Contact Information: " + contactInfo + '\n' +
                "Past Appointments: " + '\n' + getDetailedAppointmentsString(getPastAppointments());
    }

    private String getDetailedAppointmentsString(List<Appointment> appointments) {
        StringBuilder sb = new StringBuilder();
        for (Appointment appointment : appointments) {
            sb.append(appointment.getFullDetails()).append('\n');
        }
        return sb.toString();
    }

    public List<Appointment> getPastAppointments() {
        return pastAppointmentsFilter.filter(AppointmentManager.getInstance().getAppointments());
    }
}
