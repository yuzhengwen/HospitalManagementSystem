package DataHandling;

import Model.Appointment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class AppointmentSerializer implements ISerializer<Appointment> {
    private static final String SEPARATOR = ",";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
        String patientId;
        String typeString;
        String statusString;
        Appointment.Type type;
        Appointment.Status status;
        if (!tokenizer.hasMoreTokens()) { // if there are no more tokens, set the values to null, i.e. doctor is available
            patientId = null;
            typeString = null;
            statusString = null;
            type = null;
            status = null;
        }
        else{
            patientId = tokenizer.nextToken().trim();
            typeString = tokenizer.nextToken().trim();
            type = Appointment.Type.valueOf(typeString.toUpperCase());
            statusString = tokenizer.nextToken().trim();
            status = Appointment.Status.valueOf(statusString.toUpperCase());
        }
        return new Appointment(doctorId, date, patientId, type, status);
    }
}