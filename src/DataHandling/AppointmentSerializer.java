package DataHandling;

import Model.Appointment;
import Model.ScheduleManagement.TimeSlot;

import java.time.LocalDate;
import java.util.StringTokenizer;

public class AppointmentSerializer implements ISerializer<Appointment> {
    TimeSlotSerializer timeSlotSerializer = new TimeSlotSerializer();
    DateSerializer dateSerializer = new DateSerializer();

    @Override
    public String serialize(Appointment object) {
        return dateSerializer.serialize(object.getDate()) + SEPARATOR + timeSlotSerializer.serialize(object.getTimeSlot())
                + SEPARATOR + object.getPatientId() + SEPARATOR + object.getDoctorId() + SEPARATOR + object.getType();
    }

    @Override
    public Appointment deserialize(String data) {
        StringTokenizer star = new StringTokenizer(data, SEPARATOR);
        LocalDate date = dateSerializer.deserialize(star.nextToken().trim());
        TimeSlot timeSlot = timeSlotSerializer.deserialize(star.nextToken().trim());
        String patientId = star.nextToken().trim();
        String doctorId = star.nextToken().trim();
        Appointment.Type type = Appointment.Type.valueOf(star.nextToken().trim().toUpperCase());
        Appointment apt = new Appointment(patientId, date, timeSlot, type);
        if (!doctorId.isEmpty())
            apt.setDoctorId(doctorId);
        return apt;
    }
}
