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

    public boolean isWorking(DayOfWeek dayOfWeek, LocalTime time) {
        if (!schedule.containsKey(dayOfWeek)) {
            return false;
        }
        TimeSlot timeSlot = schedule.get(dayOfWeek);
        // check if time is within working hours for the day
        return (time.isAfter(timeSlot.getStart()) || time.equals(timeSlot.getStart())) && time.isBefore(timeSlot.getEnd());
    }

    public List<TimeSlot> getAvailableTimeSlotsOnDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return new ArrayList<>(timeSlots.get(dayOfWeek));
    }

    public Staff getStaff() {
        return doctor;
    }
}

