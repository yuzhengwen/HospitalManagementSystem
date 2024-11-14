package Model;

import CustomTypes.PrescriptionStatus;

public class Prescription {
    private String prescriptionId;
    private String medicationName;
    private PrescriptionStatus status;

    public Prescription(String prescriptionId, String medicationName) {
        this.prescriptionId = prescriptionId;
        this.medicationName = medicationName;
        this.status = PrescriptionStatus.PENDING;
    }

    public Prescription(String prescriptionId, String medicationName, PrescriptionStatus status) {
        this.prescriptionId = prescriptionId;
        this.medicationName = medicationName;
        this.status = status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public String getId() {
        return prescriptionId;
    }

    @Override
    public String toString() {
        return "Prescription Id= '" + prescriptionId + '\'' +
                ", Medication= '" + medicationName + '\'' +
                ", Status= " + status;
    }
}
