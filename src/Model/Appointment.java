package Model;

import Model.ScheduleManagement.Schedule;
import Model.ScheduleManagement.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
    public String toString(){
        return "Patient ID: " + patientId + " Doctor ID: " + doctorId + " Type: " + type + " Status: " + status;
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
