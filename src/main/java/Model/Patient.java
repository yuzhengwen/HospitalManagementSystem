package Model;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import CustomTypes.Role;
import Singletons.AppointmentManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patient extends User {
    private LocalDate dob;
    private ContactInfo contactInfo = new ContactInfo();
    private String bloodType;
    private final List<String> diagnosisHistory = new ArrayList<>();
    private final List<String> treatmentHistory = new ArrayList<>();
    private final List<String> prescriptionHistory = new ArrayList<>();

    public Patient(String id, String password, String name, LocalDate dob, String bloodType, Gender gender) {
        super(id, password, Role.PATIENT, name, gender);
        this.dob = dob;
        this.bloodType = bloodType;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Medical Record: \n");
        sb.append("Patient ID: ").append(id).append('\n');
        sb.append("Name: ").append(name).append('\n');
        sb.append("Date of Birth: ").append(dob).append('\n');
        sb.append("Gender: ").append(gender).append('\n');
        sb.append("Blood Type: ").append(bloodType).append('\n');
        sb.append("Contact Information: ").append(contactInfo).append('\n');
        sb.append("----------------").append('\n');
        sb.append("Diagnosis History: (Oldest first)").append('\n');
        sb.append("----------------").append('\n');
        if (diagnosisHistory.isEmpty()) {
            sb.append("No diagnosis history").append('\n');
        } else {
            for (String diagnosis : diagnosisHistory) {
                sb.append(diagnosis).append('\n');
            }
        }
        sb.append("----------------").append('\n');
        sb.append("Treatment History: (Oldest first)").append('\n');
        sb.append("----------------").append('\n');
        if (treatmentHistory.isEmpty()) {
            sb.append("No treatment history").append('\n');
        } else {
            for (String treatment : treatmentHistory) {
                sb.append(treatment).append('\n');
            }
        }
        sb.append("----------------").append('\n');
        sb.append("Prescription History: (Oldest first)").append('\n');
        sb.append("----------------").append('\n');
        if (prescriptionHistory.isEmpty()) {
            sb.append("No prescriptions found").append('\n');
        } else {
            List<Prescription> prescriptions = new ArrayList<>();
            for (String prescriptionId : prescriptionHistory) {
                prescriptions.add(AppointmentManager.getInstance().getPrescriptionById(prescriptionId));
            }
            for (Prescription prescription : prescriptions) {
                sb.append(prescription.getMedicineQuantitiesString()).append('\n');
            }
        }
        return sb.toString();
    }

    public void addDiagnosis(String diagnosis) {
        diagnosisHistory.add(diagnosis);
    }

    public void addTreatment(String treatment) {
        treatmentHistory.add(treatment);
    }

    public void addPrescription(String prescriptionId) {
        prescriptionHistory.add(prescriptionId);
    }

    public List<String> getDiagnosisHistory() {
        return diagnosisHistory;
    }

    public List<String> getTreatmentHistory() {
        return treatmentHistory;
    }

    public List<String> getPrescriptionHistory() {
        return prescriptionHistory;
    }
    public void addDiagnoses(List<String> diagnoses) {
        diagnosisHistory.addAll(diagnoses);
    }
    public void addTreatments(List<String> treatments) {
        treatmentHistory.addAll(treatments);
    }
    public void addPrescriptions(List<String> prescriptions) {
        prescriptionHistory.addAll(prescriptions);
    }
}
