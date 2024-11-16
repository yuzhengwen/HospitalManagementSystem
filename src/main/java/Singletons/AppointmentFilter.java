package Singletons;

import Model.Appointment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filters appointments based on doctor, patient, date, and status
 * Uses the builder pattern to allow for optional parameters
 */
public class AppointmentFilter {
    String doctorId = null;
    String patientId = null;
    LocalDate date = null;
    Set<Appointment.Status> statuses = new HashSet<>();

    public AppointmentFilter filterByDoctor(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public AppointmentFilter filterByPatient(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public AppointmentFilter filterByDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public AppointmentFilter filterByStatus(Appointment.Status status) {
        statuses.add(status);
        return this;
    }

    /**
     * Filters the given list of appointments based on the filter criteria.
     * Original list is not modified.
     * If no filter criteria are set, the original list is returned.
     * If original list is empty, an empty list is returned
     *
     * @param appointments list of appointments before filtering
     * @return new list of appointments after filtering
     */
    public List<Appointment> filter(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            return new ArrayList<>();
        }
        List<Appointment> filteredAppointments = new ArrayList<>(appointments);
        if (doctorId != null) {
            filteredAppointments.removeIf(appointment -> !appointment.getDoctorId().equals(doctorId));
        }
        if (patientId != null) {
            filteredAppointments.removeIf(appointment -> !appointment.getPatientId().equals(patientId));
        }
        if (date != null) {
            filteredAppointments.removeIf(appointment -> !appointment.getDate().equals(date));
        }
        if (!statuses.isEmpty()) {
            filteredAppointments.removeIf(appointment -> !statuses.contains(appointment.getStatus()));
        }
        return filteredAppointments;
    }
}
