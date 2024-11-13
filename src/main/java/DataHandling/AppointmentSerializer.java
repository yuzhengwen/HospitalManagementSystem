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
    AppointmentOutcomeRecordSerializer outcomeRecordSerializer = new AppointmentOutcomeRecordSerializer();

    @Override
    public String serialize(Appointment object) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateSerializer.serialize(object.getDate())).append(SEPARATOR)
                .append(timeSlotSerializer.serialize(object.getTimeSlot())).append(SEPARATOR)
                .append(object.getPatientId()).append(SEPARATOR)
                .append(object.getDoctorId()).append(SEPARATOR)
                .append(object.getType()).append(SEPARATOR)
                .append(object.getStatus());
        if (object.getOutcome() != null) {
            sb.append(SEPARATOR).append(outcomeRecordSerializer.serialize(object.getOutcome()));
        }
        return sb.toString();
    }

    @Override
    public Appointment deserialize(String data) {
        StringTokenizer star = new StringTokenizer(data, SEPARATOR);
        LocalDate date = dateSerializer.deserialize(star.nextToken().trim());
        TimeSlot timeSlot = timeSlotSerializer.deserialize(star.nextToken().trim());
        String patientId = star.nextToken().trim();
        String doctorId = star.nextToken().trim();
        Appointment.Type type = Appointment.Type.valueOf(star.nextToken().trim().toUpperCase());
        Appointment.Status status = Appointment.Status.valueOf(star.nextToken().trim().toUpperCase());
        Appointment apt = new Appointment(patientId, date, timeSlot, type);
        apt.setStatus(status);
        if (star.hasMoreTokens()) {
            StringBuilder serializedOutcomeBuilder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                serializedOutcomeBuilder.append(star.nextToken().trim());
                if (i < 3) {
                    serializedOutcomeBuilder.append(SEPARATOR);
                }
            }
            String serializedOutcome = serializedOutcomeBuilder.toString();
            AppointmentOutcomeRecord outcome = outcomeRecordSerializer.deserialize(serializedOutcome);
            apt.setOutcome(outcome);
        }
        if (!doctorId.isEmpty())
            apt.setDoctorId(doctorId);
        return apt;
    }

    static class AppointmentOutcomeRecordSerializer implements ISerializer<AppointmentOutcomeRecord> {
        PrescriptionSerializer prescriptionSerializer = new PrescriptionSerializer();

        @Override
        public String serialize(AppointmentOutcomeRecord object) {
            return object.getServiceProvided() + SEPARATOR +
                    object.getNotes() + SEPARATOR +
                    prescriptionSerializer.serialize(object.getPrescription());
        }

        @Override
        public AppointmentOutcomeRecord deserialize(String data) {
            StringTokenizer star = new StringTokenizer(data, SEPARATOR);
            ServiceProvided service = ServiceProvided.valueOf(star.nextToken().trim().toUpperCase());
            String notes = star.nextToken().trim();
            String serializedPrescription = star.nextToken().trim() + SEPARATOR + star.nextToken().trim();
            Prescription prescription = prescriptionSerializer.deserialize(serializedPrescription);
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
