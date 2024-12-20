package Model.ScheduleManagement;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TimeSlot {
    private final LocalTime start;
    private final LocalTime end;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }
    public TimeSlot(int startHour, int endHour) {
        this.start = LocalTime.of(startHour, 0);
        this.end = LocalTime.of(endHour, 0);
    }

    // Getters for start and end time
    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    // Overriding equals to compare time slots
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) obj;
        return start.equals(timeSlot.start) && end.equals(timeSlot.end);
    }

    // Time slots are equal if they have the same start and end time (For Map comparison/hashing)
    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        String startTime = start.format(formatter);
        String endTime = end.format(formatter);
        return startTime + " - " + endTime;
    }

    /**
     * Time is within the time slot if it is equal to the start time or between the start and end time
     * @param time LocalTime to check
     * @return true if time is within the time slot
     */
    public boolean isTimeWithin(LocalTime time) {
        return time.equals(start) || (time.isAfter(start) && time.isBefore(end));
    }
}
