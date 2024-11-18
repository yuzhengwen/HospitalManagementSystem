import DataHandling.DoctorScheduleSerializer;
import Model.ScheduleManagement.Schedule;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

public class ScheduleTests {
    @Test
    public void testDoctorScheduleSerializer() {
        DoctorScheduleSerializer doctorScheduleSerializer = new DoctorScheduleSerializer();
        DoctorScheduleSerializer.ScheduleSerializer scheduleSerializer = new DoctorScheduleSerializer.ScheduleSerializer();

        Schedule schedule = new Schedule(null);
        schedule.setWorkingHours(DayOfWeek.MONDAY, 8, 16);
        schedule.setWorkingHours(DayOfWeek.TUESDAY, 8, 16);
        schedule.setWorkingHours(DayOfWeek.WEDNESDAY, 8, 16);
        schedule.setWorkingHours(DayOfWeek.THURSDAY, 8, 16);
        schedule.setWorkingHours(DayOfWeek.FRIDAY, 8, 16);
        String serialized = scheduleSerializer.serialize(schedule);
        String serializedString = "08:00-16:00,08:00-16:00,08:00-16:00,08:00-16:00,08:00-16:00,,";
        assert serialized.equalsIgnoreCase(serializedString);
        Schedule deserialized = scheduleSerializer.deserialize(serialized);
        String result = """
                Schedule:
                Monday: 08:00 - 16:00
                Tuesday: 08:00 - 16:00
                Wednesday: 08:00 - 16:00
                Thursday: 08:00 - 16:00
                Friday: 08:00 - 16:00
                """;
        assert deserialized.printScheduleCompact().equalsIgnoreCase(result);
    }
}
