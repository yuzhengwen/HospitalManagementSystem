package Model;

import CustomTypes.ServiceProvided;

import java.util.Date;

public class AppointmentOutcomeRecord {
    private Prescription prescription;
    private ServiceProvided serviceProvided;
    private String notes;

    public AppointmentOutcomeRecord(Prescription prescription, ServiceProvided serviceProvided, String notes) {
        this.prescription = prescription;
        this.serviceProvided = serviceProvided;
        this.notes = notes;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public ServiceProvided getServiceProvided() {
        return serviceProvided;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Prescription: " + prescription + '\n' +
                "ServiceProvided: " + serviceProvided + '\n' +
                "Notes: " + notes;
    }
}
