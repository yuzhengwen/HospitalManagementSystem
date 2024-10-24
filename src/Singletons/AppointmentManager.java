package Singletons;

import Model.Appointment;

import java.util.ArrayList;
import java.util.Date;

public class AppointmentManager {
    private static AppointmentManager instance;
    private AppointmentManager(){}
    public static synchronized AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    private final ArrayList<Appointment> appointments = new ArrayList<>();
    private final ArrayList<Date> availableDates = new ArrayList<>();

    public void updateAvailableDates(){
        // TODO implement this properly
        availableDates.add(new Date(2313223232L));
        availableDates.add(new Date(2343223235L));
    }
    public ArrayList<Date> getAvailableDates() {
        return availableDates;
    }

    public void add(Appointment appointment) {
        appointments.add(appointment);
    }
    public void remove(Appointment appointment) {
        appointments.remove(appointment);
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
}
