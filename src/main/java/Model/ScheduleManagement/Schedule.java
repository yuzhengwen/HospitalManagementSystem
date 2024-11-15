package Model.ScheduleManagement;

import Model.Staff;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Schedule {
    private Staff doctor;
    /**
     * Map of DayOfWeek to TimeSlot representing the working hours for each day
     * E.g. One entry could be Monday: 09:00 - 17:00
     * Uses TreeMap to sort the days in order
     */
    private final Map<DayOfWeek, TimeSlot> schedule = new TreeMap<>();
    /**
     * Map of DayOfWeek to List of TimeSlot representing the available time slots for each day
     * Each TimeSlot is 60 minutes long (APPOINTMENT_DURATION)
     * E.g. One entry could be Monday: [09:00 - 10:00, 10:00 - 11:00, ...]
     * Uses TreeMap to sort the days in order
     */
    private final Map<DayOfWeek, List<TimeSlot>> timeSlots = new TreeMap<>();

    public static final int APPOINTMENT_DURATION = 60;

    public Schedule(Staff doctor) {
        this.doctor = doctor;
    }
    public void setDoctor(Staff doctor) {
        this.doctor = doctor;
    }

    public void setWorkingHours(DayOfWeek day, int startHour, int endHour) {
        LocalTime start = LocalTime.of(startHour, 0);
        LocalTime end = LocalTime.of(endHour, 0);
        schedule.put(day, new TimeSlot(start, end));
        updateTimeSlots();
    }
    public void setWorkingHours(DayOfWeek day, TimeSlot timeSlot) {
        setWorkingHours(day, timeSlot.getStart().getHour(), timeSlot.getEnd().getHour());
    }

    private void updateTimeSlots() {
        timeSlots.clear();
        for (DayOfWeek day : schedule.keySet()) {
            timeSlots.put(day, splitTimeSlot(schedule.get(day)));
        }
    }

    /**
     * Split the working hours into 60 minute time slots
     * E.g. 09:00 - 17:00 -> [09:00 - 10:00, 10:00 - 11:00, ...]
     * @param timeSlot Working hours for the day
     * @return List of TimeSlot representing the available time slots
     */
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

    /**
     * Print the schedule in a readable format
     * E.g.
     * Schedule:
     * Monday: 09:00 - 17:00
     * Tuesday: 09:00 - 17:00
     * @return String representing the schedule
     */
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
        if (!timeSlots.containsKey(dayOfWeek)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(timeSlots.get(dayOfWeek));
    }

    public Staff getStaff() {
        return doctor;
    }
    public Map<DayOfWeek, TimeSlot> getSchedule() {
        return schedule;
    }
}

