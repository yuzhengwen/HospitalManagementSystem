package Model.ScheduleManagement;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class Schedule {
    private final Map<DayOfWeek, TimeSlot> schedule = new HashMap<>();
    private final Map<DayOfWeek, List<TimeSlot>> timeSlots = new HashMap<>();

    private final int APPOINTMENT_DURATION = 60;

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
    public void getAvailableTimeSlots(DayOfWeek day) {
        for (TimeSlot slot : timeSlots.get(day)) {
            if (slot.available) {
                System.out.println(slot);
            }
        }
    }
    public void bookTimeSlot(DayOfWeek day, TimeSlot timeSlot) {
        for (TimeSlot slot : timeSlots.get(day)) {
            if (slot.equals(timeSlot)) {
                slot.available = false;
            }
        }
    }

    // for testing
    public static void main(String[] args) {
        Schedule schedule = new Schedule();
        schedule.setWorkingHours(DayOfWeek.MONDAY, 8, 20);
        schedule.setWorkingHours(DayOfWeek.TUESDAY, 10, 12);
        schedule.setWorkingHours(DayOfWeek.WEDNESDAY, 8, 17);
        schedule.setWorkingHours(DayOfWeek.THURSDAY, 8, 17);
        schedule.setWorkingHours(DayOfWeek.FRIDAY, 8, 17);
    }
}

