package Model.ScheduleManagement;

import Model.Staff;

import java.util.List;

public class TimeSlotWithDoctor {
    private final TimeSlot timeSlot;
    private final List<Staff> doctor;

    public TimeSlotWithDoctor(TimeSlot timeSlot, List<Staff> doctor) {
        this.timeSlot = timeSlot;
        this.doctor = doctor;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public List<Staff> getDoctors() {
        return doctor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(timeSlot.toString()).append(": ");
        for (Staff staff : doctor) {
            sb.append(staff.getName()).append(" ");
        }
        return sb.toString();
    }
}
