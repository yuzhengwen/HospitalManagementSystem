package Singletons;

import Model.Appointment;
import Model.Staff;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AppointmentManager {
    private static AppointmentManager instance;

    
    public static synchronized AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    private final ArrayList<Appointment> appointments = new ArrayList<>();

    public void add(Appointment appointment) {
        appointments.add(appointment);
    }

    public void remove(Appointment appointment) {
        appointments.remove(appointment);
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Date> getAvailableDates() {
        Set<Date> availableDates = new HashSet<>();

        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() == null &&
                appointment.getType() == null &&
                appointment.getStatus() == null) {
                availableDates.add(appointment.getDate());
            }
        }
        ArrayList<Date> sortedAvailableDates = new ArrayList<>(availableDates);
        sortedAvailableDates.sort(Date::compareTo);
        return sortedAvailableDates;
    }

    public ArrayList<Appointment> getAppointmentsByPatientId(String patientId) {
        ArrayList<Appointment> appointmentsByPatient = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() != null) { // only add appointments with patients
                if (appointment.getPatientId().equals(patientId)) {
                    appointmentsByPatient.add(appointment);
                }
            }
        }
        appointmentsByPatient.sort((a1, a2) -> a1.getDate().compareTo(a2.getDate()));
        return appointmentsByPatient;
    }

    public ArrayList<Appointment> getAppointmentsByDoctorId(String doctorId) {
        ArrayList<Appointment> appointmentsByDoctor = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(doctorId) && appointment.getPatientId() != null) { // only add appointments with patients
                appointmentsByDoctor.add(appointment);
            }
        }
        appointmentsByDoctor.sort((a1, a2) -> a1.getDate().compareTo(a2.getDate()));
        return appointmentsByDoctor;
    }
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public ArrayList<Staff> getAvailableDoctors(Date date) {
        Set<String> availableDoctorIds = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Appointment appointment : appointments) {
            if (dateFormat.format(appointment.getDate()).equals(dateFormat.format(date)) &&
                appointment.getPatientId() == null &&
                appointment.getType() == null &&
                appointment.getStatus() == null) {
                availableDoctorIds.add(appointment.getDoctorId());
            }
        }

        ArrayList<Staff> availableDoctors = new ArrayList<>();
        for (String doctorId : availableDoctorIds) {
            Staff doctor = (Staff)UserLoginManager.getInstance().getUserById(doctorId);
            if (doctor != null) {
                availableDoctors.add(doctor);
            }
        }

        return availableDoctors;
    }
}
