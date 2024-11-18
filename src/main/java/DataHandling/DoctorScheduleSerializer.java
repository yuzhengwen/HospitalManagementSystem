package DataHandling;

import Model.ScheduleManagement.Schedule;
import Model.ScheduleManagement.TimeSlot;
import Model.Staff;
import Singletons.UserLoginManager;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.StringTokenizer;

public class DoctorScheduleSerializer implements ISerializer<Map<String, Schedule>> {
    ScheduleSerializer scheduleSerializer = new ScheduleSerializer();

    @Override
    public String serialize(Map<String, Schedule> object) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Schedule> entry : object.entrySet()) {
            sb.append(entry.getKey()).append(SEPARATOR).append(scheduleSerializer.serialize(entry.getValue())).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Map<String, Schedule> deserialize(String data) {
        StringTokenizer star = new StringTokenizer(data, "\n");
        Map<String, Schedule> doctorSchedules = new java.util.HashMap<>();
        while (star.hasMoreTokens()) {
            String line = star.nextToken();
            int firstSeparatorIndex = line.indexOf(SEPARATOR);

            String doctorId = line.substring(0, firstSeparatorIndex);
            String scheduleData = line.substring(firstSeparatorIndex + 1);

            Staff doctor = (Staff) UserLoginManager.getInstance().getUserById(doctorId);
            Schedule schedule = scheduleSerializer.deserialize(scheduleData);
            schedule.setDoctor(doctor);
            doctorSchedules.put(doctorId, schedule);
        }
        return doctorSchedules;
    }

    /**
     * Internal class to serialize and deserialize Schedule objects
     * Only used by DoctorScheduleSerializer
     */
    public static class ScheduleSerializer implements ISerializer<Schedule> {
        TimeSlotSerializer timeSlotSerializer = new TimeSlotSerializer();

        @Override
        public String serialize(Schedule object) {
            Map<DayOfWeek, TimeSlot> schedule = object.getSchedule();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                if (schedule.containsKey(DayOfWeek.values()[i])) {
                    sb.append(timeSlotSerializer.serialize(schedule.get(DayOfWeek.values()[i]))).append(SEPARATOR);
                } else {
                    sb.append(SEPARATOR);
                }
            }
            if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == SEPARATOR.charAt(0))
                sb.deleteCharAt(sb.length() - 1); // remove the last separator
            return sb.toString();
        }

        @Override
        public Schedule deserialize(String data) {
            String[] timeSlots = data.split(SEPARATOR);
            Schedule schedule = new Schedule(null);
            for (int i = 0; i < timeSlots.length; i++) {
                if (!timeSlots[i].isEmpty()) {
                    schedule.setWorkingHours(DayOfWeek.values()[i], timeSlotSerializer.deserialize(timeSlots[i]));
                }
            }
            return schedule;
        }
    }
}
