package Model;

import Model.ScheduleManagement.TimeSlot;
import Singletons.UserLoginManager;

import java.time.LocalDate;

public class Appointment {
    private String patientId;
    private String doctorId;
    private Type type;
    private Status status;
    private AppointmentOutcomeRecord outcome;
    private LocalDate date;
    private TimeSlot timeSlot;

    public Appointment(String patientId, LocalDate date, TimeSlot timeSlot, Type type) {
        this.patientId = patientId;
        this.type = type;
        this.status = Status.PENDING;
        this.timeSlot = timeSlot;
        this.date = date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "Date/Time: " + date + " " + timeSlot + " Patient ID: " + patientId + " Doctor ID: " + doctorId + " Type: " + type + " Status: " + status;
    }

    public String getFullDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(date).append("\n");
        sb.append("Time: ").append(timeSlot).append("\n");
        sb.append("Patient Name: ").append((UserLoginManager.getInstance().getUserById(patientId)).getName()).append("\n");
        sb.append("Patient ID: ").append(patientId).append("\n");
        sb.append("Doctor Name: ").append((UserLoginManager.getInstance().getUserById(doctorId)).getName()).append("\n");
        sb.append("Doctor ID: ").append(doctorId).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Appointment Outcome Record: ");
        if (status == Status.COMPLETED && outcome != null) {
            sb.append(outcome).append("\n");
        } else {
            sb.append("N/A").append("\n");
        }
        return sb.toString();
    }

    public LocalDate getDate() {
        return date;
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
