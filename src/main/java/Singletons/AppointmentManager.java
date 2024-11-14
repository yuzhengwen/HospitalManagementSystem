package Singletons;

import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.Prescription;
import Model.ScheduleManagement.Schedule;
import Model.ScheduleManagement.TimeSlot;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import Model.Staff;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AppointmentManager {
    private static AppointmentManager instance;

    public static synchronized AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    private final Map<String, Schedule> doctorScheduleMap = new HashMap<>();
    private final ArrayList<Appointment> appointments = new ArrayList<>();

    public Map<String, Schedule> getDoctorScheduleMap() {
        return doctorScheduleMap;
    }

    public void setSchedule(String doctorId, Schedule schedule) {
        doctorScheduleMap.put(doctorId, schedule);
    }

    /**
     * Get a list of appointments with a given filter
     * Other filter methods use this as a base
     * @param filter to apply to the list of appointments
     * @return a list of appointments that match the filter
     */
    public List<Appointment> getAppointmentsWithFilter(AppointmentFilter filter) {
        return filter.filter(appointments);
    }
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        return getAppointmentsWithFilter(new AppointmentFilter().filterByPatient(patientId));
    }
    public List<Appointment> getAppointmentsByStatus(Appointment.Status status){
        return getAppointmentsWithFilter(new AppointmentFilter().filterByStatus(status));
    }

    public List<Appointment> getAppointmentsByDoctorId(String id, Appointment.Status status) {
        List<Appointment> appointmentsByDoctor = new ArrayList<>();
        if (appointments.isEmpty()) {
            return appointmentsByDoctor;
        }
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId() == null || appointment.getDoctorId().isEmpty()) continue;
            if (appointment.getDoctorId().equals(id) && appointment.getStatus() == status) {
                appointmentsByDoctor.add(appointment);
            }
        }
        return appointmentsByDoctor;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void add(Appointment appointment) {
        appointments.add(appointment);
    }

    public void remove(Appointment appointment) {
        appointments.remove(appointment);
    }

    public Schedule getScheduleOfDoctor(Staff doctor) {
        if (doctorScheduleMap.containsKey(doctor.getId())) {
            return doctorScheduleMap.get(doctor.getId());
        }
        return new Schedule(doctor);
    }

    /**
     * Create a map of TimeSlot to List of Staff objects for a given date<br/>
     * Used internally for getTimeslotWithDoctorList()
     */
    private Map<TimeSlot, List<Staff>> getTimeslotToDoctorMap(LocalDate date) {
        Map<TimeSlot, List<Staff>> timeslotToDoctorMap = new HashMap<>();
        // reverse map doctor to timeslot
        for (Map.Entry<String, Schedule> entry : doctorScheduleMap.entrySet()) {
            Schedule schedule = entry.getValue();
            for (TimeSlot timeSlot : schedule.getAvailableTimeSlotsOnDate(date)) {
                // create new entry if timeslot is not in the map
                if (!timeslotToDoctorMap.containsKey(timeSlot))
                    timeslotToDoctorMap.put(timeSlot, new ArrayList<>());
                // add doctor to the timeslot
                timeslotToDoctorMap.get(timeSlot).add(schedule.getStaff());
            }
        }
        // remove timeslots that are already booked
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getStatus() == Appointment.Status.ACCEPTED) {
                timeslotToDoctorMap.get(appointment.getTimeSlot()).removeIf(doctor -> doctor.getId().equals(appointment.getDoctorId()));
            }
        }
        // remove empty timeslots
        timeslotToDoctorMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        return timeslotToDoctorMap;
    }

    /**
     * Get a list of TimeSlotWithDoctor objects for a given date<br/>
     * Each entry in the list contains a TimeSlot object and a list of available Staff
     */
    public List<TimeSlotWithDoctor> getTimeslotWithDoctorList(LocalDate date) {
        Map<TimeSlot, List<Staff>> timeslotToDoctorMap = getTimeslotToDoctorMap(date);
        List<TimeSlotWithDoctor> timeSlotWithDoctorList = new ArrayList<>();
        for (Map.Entry<TimeSlot, List<Staff>> entry : timeslotToDoctorMap.entrySet()) {
            timeSlotWithDoctorList.add(new TimeSlotWithDoctor(entry.getKey(), entry.getValue()));
        }
        // sort time slots by start time
        timeSlotWithDoctorList.sort(Comparator.comparing(ts -> ts.getTimeSlot().getStart()));
        return timeSlotWithDoctorList;
    }

    /**
     * gets a list of appointments that are pending and within the available time of the doctor
     */
    /*
    public List<Appointment> getPendingAppointmentsWithinAvailableTime(String doctorId) {
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            // check if appointment is pending and doctor is available and the appointment is for the doctor
            if (appointment.getStatus() == Appointment.Status.PENDING && Objects.equals(appointment.getDoctorId(), doctorId) &&
                    isDoctorAvailable(doctorId, appointment.getDate(), appointment.getTimeSlot().getStart())) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;
    }*/

    /**
     * Check if a doctor is available at a given date and time <br/>
     * Will check based on the doctor's schedule and appointments accepted by the doctor
     */
    public boolean isDoctorAvailable(String doctorId, LocalDate date, LocalTime time) {
        Schedule schedule = doctorScheduleMap.get(doctorId);
        // check if doctor is working on the given day and time
        if (schedule.isWorking(date.getDayOfWeek(), time)) {
            // check if doctor has an appointment at the given time
            List<Appointment> acceptedAppointments = getAppointmentsByDoctorId(doctorId, Appointment.Status.ACCEPTED);
            if (acceptedAppointments == null || acceptedAppointments.isEmpty()) {
                return true;
            }
            for (Appointment appointment : acceptedAppointments) {
                if (appointment.getDate().equals(date) && appointment.getTimeSlot().isTimeWithin(time)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Accept an appointment request
     * Sets the status of the appointment to ACCEPTED and assigns the doctor to the appointment
     *
     * @param appointment the appointment to accept
     * @param doctorId    the id of the doctor accepting the appointment
     */
    public void acceptAppointment(Appointment appointment, String doctorId) {
        appointment.setDoctorId(doctorId);
        appointment.setStatus(Appointment.Status.ACCEPTED);
    }

    /**
     * record the outcome of an appointment
     * @param doctorId the id of the doctor recording the outcome
     * @param appointment the appointment to record the outcome for
     * @param outcome the outcome of the appointment
     */
    public void recordAppointmentOutcome(String doctorId, Appointment appointment, AppointmentOutcomeRecord outcome) {
        appointment.setOutcome(outcome);
        appointment.setStatus(Appointment.Status.COMPLETED);
    }

    public List<AppointmentOutcomeRecord> getAllOutcomes() {
        List<Appointment> completedAppointments = getAppointmentsByStatus(Appointment.Status.COMPLETED);
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        for (Appointment appointment : completedAppointments) {
            if (appointment.getOutcome() != null) {
                records.add(appointment.getOutcome());
            }
        }
        return records;
    }
}

