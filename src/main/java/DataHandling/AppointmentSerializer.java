package DataHandling;

import CustomTypes.PrescriptionStatus;
import CustomTypes.ServiceProvided;
import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.Prescription;
import Model.ScheduleManagement.TimeSlot;
import Singletons.AppointmentManager;

import java.time.LocalDate;

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
        String[] parts = data.split(SEPARATOR, -1);
        LocalDate date = dateSerializer.deserialize(parts[0].trim());
        TimeSlot timeSlot = timeSlotSerializer.deserialize(parts[1].trim());
        String patientId = parts[2].trim();
        String doctorId = parts[3].trim();
        Appointment.Type type = Appointment.Type.valueOf(parts[4].trim().toUpperCase());
        Appointment.Status status = Appointment.Status.valueOf(parts[5].trim().toUpperCase());
        Appointment apt = new Appointment(patientId, date, timeSlot, type);
        apt.setStatus(status);
        // check if there is an outcome record
        if (parts.length > 6) {
            // Build back the format: "ServiceProvided,Notes,PrescriptionId"
            StringBuilder serializedOutcomeBuilder = new StringBuilder();
            for (int i = 6; i < parts.length; i++) {
                serializedOutcomeBuilder.append(parts[i].trim());
                if (i < parts.length - 1) {
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

        // Format: ServiceProvided,Notes,PrescriptionId
        @Override
        public String serialize(AppointmentOutcomeRecord object) {
            return object.getServiceProvided() + SEPARATOR +
                    StringUtils.addQuotes(object.getNotes()) + SEPARATOR +
                    object.getPrescription().getPrescriptionId();
        }

        @Override
        public AppointmentOutcomeRecord deserialize(String data) {
            //String[] parts = data.split(SEPARATOR, -1);
            String[] parts = StringUtils.parseLine(data);
            ServiceProvided service = ServiceProvided.valueOf(parts[0].trim().toUpperCase());
            String notes = parts[1].trim();
            String prescriptionId = parts[2].trim();
            return new AppointmentOutcomeRecord(prescriptionId, service, notes);
        }
    }


}
