package Model;

import java.util.Date;

public class Appointment {
    private String patientId;
    private String doctorId;
    private Type type;
    private Status status;
    private AppointmentOutcomeRecord outcome;
    private Date date;

    public Appointment(String patientId, Date date, Type type) {
        this.patientId = patientId;
        this.type = type;
        this.status = Status.PENDING;
        this.date = date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString(){
        return "Patient ID: " + patientId + " Doctor ID: " + doctorId + " Type: " + type + " Status: " + status;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public String getPatientId() {
        return patientId;
    }
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public AppointmentOutcomeRecord getOutcome() {
        return outcome;
    }

    public void setOutcome(AppointmentOutcomeRecord outcome) {
        this.outcome = outcome;
    }

    public enum Type {
        CHECKUP,
        FOLLOWUP,
        EMERGENCY
    }
    public enum Status {
        PENDING,
        ACCEPTED,
        CANCELLED,
        COMPLETED
    }
}
