package DataHandling;

import Model.ScheduleManagement.TimeSlot;

public class TimeSlotSerializer implements ISerializer<TimeSlot> {
    TimeSerializer timeSerializer = new TimeSerializer();

    @Override
    public String serialize(TimeSlot object) {
        return timeSerializer.serialize(object.getStart()) + SEPARATOR + timeSerializer.serialize(object.getEnd());
    }

    @Override
    public TimeSlot deserialize(String data) {
        String[] parts = data.split(SEPARATOR);
        return new TimeSlot(timeSerializer.deserialize(parts[0]), timeSerializer.deserialize(parts[1]));
    }
}
