package Model.ScheduleManagement;

import Model.Staff;
import Singletons.AppointmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeSlotWithDoctor {
    private final TimeSlot timeSlot;
    private final List<Staff> doctors;
    private final boolean[] availability;
    private boolean patientBusy;

    public TimeSlotWithDoctor(TimeSlot timeSlot, List<Staff> doctors, boolean[] availability) {
        this.timeSlot = timeSlot;
        this.doctors = doctors;
        this.availability = availability;
    }

    public void setPatientBusy(boolean patientBusy) {
        this.patientBusy = patientBusy;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public List<Staff> getDoctors() {
        return doctors;
    }
    public List<Staff> getAvailableDoctors() {
        List<Staff> availableDoctors = new ArrayList<>();
        for (int i = 0; i < doctors.size(); i++) {
            if (availability[i]) {
                availableDoctors.add(doctors.get(i));
            }
        }
        return availableDoctors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (patientBusy) sb.append("(Patient Busy) ");
        sb.append(timeSlot.toString()).append(": ").append("Doctors: ");
        for (int i = 0; i < doctors.size(); i++) {
            if (availability[i]) {
                sb.append(doctors.get(i).getName()).append(", ");
            } else {
                sb.append(doctors.get(i).getName()).append(" (Booked), ");
            }
        }
        return sb.toString();
    }

    public boolean isAvailable() {
        if (patientBusy) return false;
        for (boolean b : availability) {
            if (!b) return false;
        }
        return true;
    }
}
