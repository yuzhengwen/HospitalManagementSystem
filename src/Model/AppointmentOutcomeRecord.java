package Model;

import CustomTypes.ServiceProvided;

import java.util.Date;

public class AppointmentOutcomeRecord {
    private Date date;
    private Prescription prescription;
    private ServiceProvided serviceProvided;
    private String notes;

    public AppointmentOutcomeRecord(Date date, Prescription prescription, ServiceProvided serviceProvided, String notes) {
        this.date = date;
        this.prescription = prescription;
        this.serviceProvided = serviceProvided;
        this.notes = notes;
    }
}
