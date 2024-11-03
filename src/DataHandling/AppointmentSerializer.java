package DataHandling;

import CustomTypes.ServiceProvided;
import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.Prescription;
import Model.ScheduleManagement.TimeSlot;

import java.time.LocalDate;
import java.util.StringTokenizer;

public class AppointmentSerializer implements ISerializer<Appointment> {
    TimeSlotSerializer timeSlotSerializer = new TimeSlotSerializer();
    DateSerializer dateSerializer = new DateSerializer();
    // TODO add outcome serializer (Should prescriptions be serialized separately?)
    AppointmentOutcomeRecordSerializer outcomeRecordSerializer = new AppointmentOutcomeRecordSerializer();

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

    static class AppointmentOutcomeRecordSerializer implements ISerializer<AppointmentOutcomeRecord> {
        PrescriptionSerializer prescriptionSerializer = new PrescriptionSerializer();

        @Override
        public String serialize(AppointmentOutcomeRecord object) {
            return prescriptionSerializer.serialize(object.getPrescription()) + SEPARATOR +
                    object.getServiceProvided() + SEPARATOR + object.getNotes();
        }

        @Override
        public AppointmentOutcomeRecord deserialize(String data) {
            StringTokenizer star = new StringTokenizer(data, SEPARATOR);
            Prescription prescription = prescriptionSerializer.deserialize(star.nextToken().trim());
            ServiceProvided service = ServiceProvided.valueOf(star.nextToken().trim().toUpperCase());
            String notes = star.nextToken().trim();
            return new AppointmentOutcomeRecord(prescription, service, notes);
        }
    }

    static class PrescriptionSerializer implements ISerializer<Prescription> {
        @Override
        public String serialize(Prescription object) {
            return object.getId() + SEPARATOR + object.getMedicationName();
        }

        @Override
        public Prescription deserialize(String data) {
            StringTokenizer star = new StringTokenizer(data, SEPARATOR);
            String id = star.nextToken().trim();
            String medicationName = star.nextToken().trim();
            return new Prescription(id, medicationName);
        }
    }
}
