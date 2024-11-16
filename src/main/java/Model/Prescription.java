package Model;

import CustomTypes.PrescriptionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prescription {
    private final String prescriptionId;
    private PrescriptionStatus status;
    private final Map<String, Integer> medicineQuantities = new HashMap<>();
    private String notes;

    public Prescription(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        this.status = PrescriptionStatus.PENDING;
    }

    public Prescription(String prescriptionId, PrescriptionStatus status) {
        this.prescriptionId = prescriptionId;
        this.status = status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public String getId() {
        return prescriptionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription ID: ").append(prescriptionId).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Medicines:\n");
        for (Map.Entry<String, Integer> entry : medicineQuantities.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        if (notes != null) {
            sb.append("Notes: ").append(notes).append("\n");
        }
        return sb.toString();
    }

    public Map<String, Integer> getMedicineQuantities() {
        return medicineQuantities;
    }
    public void addMedicine(String medicine, int quantity) {
        medicineQuantities.put(medicine, quantity);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
