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

    public void UpdateStatus(PrescriptionStatus status) {
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
}
