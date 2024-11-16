package Model;

import CustomTypes.ServiceProvided;
import Singletons.AppointmentManager;

public class AppointmentOutcomeRecord {
    private ServiceProvided serviceProvided;
    private String notes;
    private final String prescriptionId;

    public AppointmentOutcomeRecord(String prescriptionId, ServiceProvided serviceProvided, String notes) {
        this.prescriptionId = prescriptionId;
        this.serviceProvided = serviceProvided;
        this.notes = notes;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }
    public Prescription getPrescription() {
        return AppointmentManager.getInstance().getPrescriptionById(prescriptionId);
    }

    public ServiceProvided getServiceProvided() {
        return serviceProvided;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ServiceProvided: " + serviceProvided + '\n' +
                "Notes: " + notes + '\n' + "Prescriptions: \n" + getPrescription().toString();
    }
}
