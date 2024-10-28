package DataHandling;

import Model.Appointment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class AppointmentSerializer implements ISerializer<Appointment> {
    private static final String SEPARATOR = ",";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String serialize(Appointment object) {
        return object.getDoctorId() + SEPARATOR +
               dateFormat.format(object.getDate()) + SEPARATOR +
               (object.getPatientId() != null ? object.getPatientId() : "") + SEPARATOR +
               (object.getType() != null ? object.getType() : "") + SEPARATOR +
               (object.getStatus() != null ? object.getStatus() : "");
    }

    // format: doctorId,date,patientId,type,status
    @Override
    public Appointment deserialize(String data) {
        StringTokenizer tokenizer = new StringTokenizer(data, SEPARATOR);
        
        String doctorId = tokenizer.nextToken().trim();
        Date date = null;
        try {
            date = dateFormat.parse(tokenizer.nextToken().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String patientId = tokenizer.nextToken().trim();
        if (patientId.isEmpty()) {
            patientId = null;
        }
        String typeString = tokenizer.nextToken().trim();
        Appointment.Type type = null;
        if (!typeString.isEmpty()) {
            type = Appointment.Type.valueOf(typeString.toUpperCase());
        }
        String statusString = tokenizer.nextToken().trim();
        Appointment.Status status = null;
        if (!statusString.isEmpty()) {
            status = Appointment.Status.valueOf(statusString.toUpperCase());
        }

        return new Appointment(doctorId, date, patientId, type, status);
    }
}