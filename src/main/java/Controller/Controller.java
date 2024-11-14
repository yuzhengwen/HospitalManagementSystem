package Controller;

import CustomTypes.OperationMode;
import CustomTypes.Role;
import Model.Appointment;
import Model.Patient;
import Model.ScheduleManagement.TimeSlot;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import Model.Staff;
import Model.User;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import View.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static Controller instance;

    public static synchronized Controller getInstance() {
        if (instance == null) { // if instance is null, create a new instance
            instance = new Controller();
        }
        return instance;
    }

    private User currentUser;

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    private ViewObject previousView;

    public void navigateBack() {
        previousView.display();
    }

    public void setPreviousView(ViewObject view) {
        previousView = view;
    }


    /**
     * Start the main menu for the current user
     */
    public void startMainMenu() {
        if (currentUser instanceof Patient) { // if the user is a patient
            new PatientView((Patient) currentUser).display(); // downcast the user to a patient and display the patient menu
        }
        if (currentUser instanceof Staff) { // if the user is a staff
            Role role = ((Staff) currentUser).getRole(); // downcast the user to a staff and check role
            if (role == Role.DOCTOR) { // if the role is doctor
                new DoctorView((Staff) currentUser).display(); // display the doctor menu
            }
            if (role == Role.ADMINISTRATOR) {
                new AdministratorView((Staff) currentUser).display();
            }
            if (role == Role.PHARMACIST) {
                new PharmacistView((Staff) currentUser).display();
            }
        }
    }

    public Appointment manageAppointments(OperationMode mode) {
        if (OperationMode.SCHEDULE == mode) {
            // select date, timeslot, doctor, type
            LocalDate date = InputManager.getInstance().getDate();
            TimeSlotWithDoctor timeSlot = selectTimeSlot(date);
            List<Staff> doctors = timeSlot.getDoctors();
            Staff selectedDoctor = InputManager.getInstance().getSelection("Select a doctor: ", doctors);
            Appointment.Type type = selectAppointmentType();

            // create the appointment object and add it to the list
            Appointment newAppointment = new Appointment(currentUser.getId(), date, timeSlot.getTimeSlot(), type);
            newAppointment.setDoctorId(selectedDoctor.getId());
            AppointmentManager.getInstance().add(newAppointment);
            return newAppointment;
        } else if (OperationMode.EDIT == mode) {
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
            if (selected != null) {
                LocalDate date = InputManager.getInstance().getDate();
                TimeSlotWithDoctor timeSlot = selectTimeSlot(date);
                List<Staff> doctors = timeSlot.getDoctors();
                Staff selectedDoctor = InputManager.getInstance().getSelection("Select a doctor: ", doctors);
                selected.setDate(date);
                selected.setTimeSlot(timeSlot.getTimeSlot());
                selected.setDoctorId(selectedDoctor.getId());
                return selected;
            }
        } else if (OperationMode.DELETE == mode) {
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
            if (selected != null) {
                AppointmentManager.getInstance().remove(selected);
                return selected;
            }
        }
        return null;
    }

    public TimeSlotWithDoctor selectTimeSlot(LocalDate date) {
        // get timeslots with doctors for the selected date
        List<TimeSlotWithDoctor> timeSlotWithDoctors = AppointmentManager.getInstance().getTimeslotWithDoctorList(date);
        // filter out timeslots that patient has appointments with
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId());
        List<TimeSlot> patientBusyTimeSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            patientBusyTimeSlots.add(appointment.getTimeSlot());
        }
        timeSlotWithDoctors.removeIf(timeSlotWithDoctor -> patientBusyTimeSlots.contains(timeSlotWithDoctor.getTimeSlot()));
        // display the available timeslots
        SelectionView<TimeSlotWithDoctor> timeSlotSelectionView = new SelectionView<>(timeSlotWithDoctors);
        timeSlotSelectionView.display();
        return timeSlotSelectionView.getSelected();
    }

    public Appointment.Type selectAppointmentType() {
        return InputManager.getInstance().getEnum("Select appointment type: ", Appointment.Type.class);
    }

    private Appointment getSelectedAppointment(List<Appointment> list) {
        return InputManager.getInstance().getSelection("Select an appointment to edit or delete: ", list);
    }
}
