package Singletons;

import Model.Appointment;
import Model.Staff;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentManager {
    private static AppointmentManager instance;

    // nested map to store doctor availability - first key is doctorId, second key is date, boolean value is availability
    private Map<String, Map<Date, Boolean>> doctorAvailability;

    private AppointmentManager() {
        doctorAvailability = new HashMap<>();
    }
    
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

    // Sets the availability of a doctor for a specific date and time range.
    public void setAvailability(Staff staff, Date date, Date startTime, Date endTime) {
        String doctorId = staff.getId();
        
        // Initialize the doctor's availability map if it doesn't exist
        doctorAvailability.putIfAbsent(doctorId, new HashMap<>());
        
        // Create calendar instances for the date, start time, and end time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        
        // Loop through the time range
        while (startCalendar.before(endCalendar)) {
            // Set the calendar time to the current slot
            calendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE));
            
            // Get the current slot as a Date object
            Date availableDateTime = calendar.getTime();
            
            // Mark the current slot as available for the doctor
            doctorAvailability.get(doctorId).put(availableDateTime, true);
            
            // Move to the next 30-minute slot
            startCalendar.add(Calendar.MINUTE, 30);
        }
    }

}
