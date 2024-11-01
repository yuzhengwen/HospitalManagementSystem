package Singletons;

import Model.Appointment;
import Model.ScheduleManagement.CalendarUtils;
import Model.ScheduleManagement.Schedule;
import Model.ScheduleManagement.TimeSlot;
import Model.Staff;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void setSchedule(Staff doctor, Schedule schedule) {
        doctorScheduleMap.put(doctor.getId(), schedule);
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Appointment> getAppointmentsByPatientId(String patientId) {
        ArrayList<Appointment> appointmentsByPatient = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patientId)) {
                appointmentsByPatient.add(appointment);
            }
        }
        return appointmentsByPatient;
    }

    public ArrayList<Appointment> getAppointmentsByDoctorId(String doctorId) {
        ArrayList<Appointment> appointmentsByDoctor = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(doctorId)) {
                appointmentsByDoctor.add(appointment);
            }
        }
        return appointmentsByDoctor;
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

    public Map<TimeSlot, List<Staff>> getTimeslotToDoctorMap(LocalDate date) {
        Map<TimeSlot, List<Staff>> timeslotToDoctorMap = new HashMap<>();
        for (Map.Entry<String, Schedule> entry : doctorScheduleMap.entrySet()) {
            Schedule schedule = entry.getValue();
            for (TimeSlot timeSlot : schedule.getAvailableTimeSlots(date)) {
                if (!timeslotToDoctorMap.containsKey(timeSlot)) {
                    timeslotToDoctorMap.put(timeSlot, new ArrayList<>());
                }
                timeslotToDoctorMap.get(timeSlot).add(schedule.getStaff());
            }
        }
        return timeslotToDoctorMap;
    }

    public void acceptAppointment(Appointment appointment, Staff doctor) {
        appointment.setDoctorId(doctor.getId());
        appointment.setStatus(Appointment.Status.ACCEPTED);
    }
}
