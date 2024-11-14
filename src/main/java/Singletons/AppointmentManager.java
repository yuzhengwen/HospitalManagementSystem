package Singletons;

import DataHandling.SaveManager;
import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.ScheduleManagement.Schedule;
import Model.ScheduleManagement.TimeSlot;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import Model.Staff;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Singleton class to manage appointments and schedules
 */
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
     *
     * @param filter to apply to the list of appointments
     * @return a list of appointments that match the filter
     */
    public List<Appointment> getAppointmentsWithFilter(AppointmentFilter filter) {
        return filter.filter(appointments);
    }

    /**
     * Get a list of appointments for a given patient
     *
     * @param patientId the id of the patient
     * @return a list of appointments for the patient
     */
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        return getAppointmentsWithFilter(new AppointmentFilter().filterByPatient(patientId));
    }

    /**
     * Get a list of appointments with a given status
     *
     * @param status the status of the appointments to get
     * @return a list of appointments with the given status
     */
    public List<Appointment> getAppointmentsByStatus(Appointment.Status status) {
        return getAppointmentsWithFilter(new AppointmentFilter().filterByStatus(status));
    }

    /**
     * Get a list of appointments for a given doctor
     *
     * @param id     the id of the doctor
     * @param status the status of the appointments to get
     * @return a list of appointments for the doctor with the given status
     */
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

    /**
     * Get a list of appointments for a given doctor
     *
     * @return a list of appointments for the doctor
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Add an appointment to the list of appointments
     *
     * @param appointment the appointment to add
     */
    public void add(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Remove an appointment from the list of appointments
     *
     * @param appointment the appointment to remove
     */
    public void remove(Appointment appointment) {
        appointments.remove(appointment);
    }

    /**
     * Gets the schedule of a doctor
     *
     * @param doctor the doctor to get the schedule for
     * @return the schedule of the doctor
     */
    public Schedule getScheduleOfDoctor(Staff doctor) {
        if (doctorScheduleMap.containsKey(doctor.getId())) {
            return doctorScheduleMap.get(doctor.getId());
        }
        return new Schedule(doctor);
    }

    /**
     * Create a map of TimeSlot to List of doctors available for a given date<br/>
     * Used internally for getTimeslotWithDoctorList()
     *
     * @param date the date to get the map for
     * @return a map of TimeSlot to List of Staff objects
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
     * Each entry in the list contains a TimeSlot object and a list of doctors available at that time
     *
     * @param date the date to get the list for
     * @return a list of TimeSlotWithDoctor objects
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
     * Check if a doctor is available at a given date and time <br/>
     * Will check based on the doctor's schedule and appointments accepted by the doctor
     *
     * @param doctorId the id of the doctor
     * @param date     the date to check
     * @param time     the time to check
     * @return true if the doctor is available, false otherwise
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
     *
     * @param doctorId    the id of the doctor recording the outcome
     * @param appointment the appointment to record the outcome for
     * @param outcome     the outcome of the appointment
     */
    public void recordAppointmentOutcome(String doctorId, Appointment appointment, AppointmentOutcomeRecord outcome) {
        appointment.setOutcome(outcome);
        appointment.setStatus(Appointment.Status.COMPLETED);
    }

    /**
     * Get a list of all outcomes of completed appointments
     * @return a list of all outcomes of completed appointments
     */
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

    // ONLY FOR Loading Data ------------------------------------------
    /**
     * Set the list of appointments
     * Only meant for loading data from file
     * @param appointments the list of appointments to set
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
    }
    /**
     * Set the map of doctor schedules
     * Only meant for loading data from file
     * @param doctorScheduleMap the map of doctor schedules to set
     */
    public void setDoctorScheduleMap(Map<String, Schedule> doctorScheduleMap) {
        this.doctorScheduleMap.clear();
        this.doctorScheduleMap.putAll(doctorScheduleMap);
    }
}

