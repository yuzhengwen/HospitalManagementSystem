package Model.ScheduleManagement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private final LocalTime start;
    private final LocalTime end;
    public boolean available;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
        this.available = true;
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

    @Override
    public String toString() {
        String startTime = start.format(formatter);
        String endTime = end.format(formatter);
        return startTime + " - " + endTime;
    }
}
