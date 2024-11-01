package Model.ScheduleManagement;

import Model.Staff;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Schedule {
    private final Staff doctor;
    private final Map<DayOfWeek, TimeSlot> schedule = new HashMap<>();
    private final Map<DayOfWeek, List<TimeSlot>> timeSlots = new HashMap<>();

    public static final int APPOINTMENT_DURATION = 60;

    public Schedule(Staff doctor) {
        this.doctor = doctor;
    }

    public void setWorkingHours(DayOfWeek day, int startHour, int endHour) {
        LocalTime start = LocalTime.of(startHour, 0);
        LocalTime end = LocalTime.of(endHour, 0);
        schedule.put(day, new TimeSlot(start, end));
        updateTimeSlots();
    }

    private void updateTimeSlots() {
        timeSlots.clear();
        for (DayOfWeek day : schedule.keySet()) {
            timeSlots.put(day, splitTimeSlot(schedule.get(day)));
        }
    }

    private List<TimeSlot> splitTimeSlot(TimeSlot timeSlot) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime start = timeSlot.getStart();
        while (start.plusMinutes(APPOINTMENT_DURATION).isBefore(timeSlot.getEnd())) {
            timeSlots.add(new TimeSlot(start, start.plusMinutes(APPOINTMENT_DURATION)));
            start = start.plusMinutes(APPOINTMENT_DURATION);
        }
        return timeSlots;
    }

    public Map<DayOfWeek, List<TimeSlot>> getTimeSlots() {
        return timeSlots;
    }

    public void bookTimeSlot(DayOfWeek day, TimeSlot timeSlot) {
        for (TimeSlot slot : timeSlots.get(day)) {
            if (slot.equals(timeSlot)) {
                slot.available = false;
            }
        }
    }

    public String printScheduleCompact() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schedule:\n");
        if (schedule.isEmpty()) {
            sb.append("No availability set\n");
            return sb.toString();
        }
        for (DayOfWeek day : schedule.keySet()) {
            sb.append(day).append(": ").append(schedule.get(day)).append("\n");
        }
        return sb.toString();
    }

    // for testing
    public static void main(String[] args) {
        Schedule schedule = new Schedule(null);
        schedule.setWorkingHours(DayOfWeek.MONDAY, 8, 20);
        schedule.setWorkingHours(DayOfWeek.TUESDAY, 10, 12);
        schedule.setWorkingHours(DayOfWeek.WEDNESDAY, 8, 17);
        schedule.setWorkingHours(DayOfWeek.THURSDAY, 8, 17);
        schedule.setWorkingHours(DayOfWeek.FRIDAY, 8, 17);
    }

    public boolean isWorking(DayOfWeek dayOfWeek, LocalTime time) {
        if (!schedule.containsKey(dayOfWeek)) {
            return false;
        }
        TimeSlot timeSlot = schedule.get(dayOfWeek);
        // check if time is within working hours for the day
        return (time.isAfter(timeSlot.getStart()) || time.equals(timeSlot.getStart())) && time.isBefore(timeSlot.getEnd());
    }

    public List<TimeSlot> getAvailableTimeSlots(LocalDate date) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        for (TimeSlot timeSlot : timeSlots.get(dayOfWeek)) {
            if (timeSlot.available) {
                availableTimeSlots.add(timeSlot);
            }
        }
        return availableTimeSlots;
    }

    public Staff getStaff() {
        return doctor;
    }
}

