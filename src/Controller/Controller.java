package Controller;

import CustomTypes.OperationMode;
import CustomTypes.Role;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.Patient;
import Model.ScheduleManagement.TimeSlot;
import Model.Staff;
import Model.User;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import View.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {
    private static Controller instance;
    private final SaveManager saveManager = new SaveManager();

    public SaveManager getSaveManager() {
        return saveManager;
    }

    private Controller() {
        saveManager.loadAppointments();
        saveManager.loadDoctorSchedules();
    }

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
                //new AdminView(currentUser).display();
            }
            if (role == Role.PHARMACIST) {
                //new PharmacistView(currentUser).display();
            }
        }
    }

    public Appointment manageAppointments(OperationMode mode) {
        if (OperationMode.SCHEDULE == mode) {
            LocalDate date = InputManager.getInstance().getDate();
            TimeSlot timeSlot = selectTimeSlot(date);
            Appointment.Type type = selectAppointmentType();
            Appointment newAppointment = new Appointment(currentUser.getId(), date, timeSlot, type);
            AppointmentManager.getInstance().add(newAppointment);
            return newAppointment;
        } else if (OperationMode.EDIT == mode) {
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
            if (selected != null) {
                LocalDate date = InputManager.getInstance().getDate();
                TimeSlot timeSlot = selectTimeSlot(date);
                selected.setDate(date);
                selected.setTimeSlot(timeSlot);
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

    public TimeSlot selectTimeSlot(LocalDate date) {
        SelectionView<TimeSlotWithDoctor> timeSlotSelectionView = new SelectionView<>(AppointmentManager.getInstance().getTimeslotWithDoctorList(date));
        timeSlotSelectionView.display();
        return timeSlotSelectionView.getSelected().getTimeSlot();
    }

    public Appointment.Type selectAppointmentType() {
        return InputManager.getInstance().getEnum("Select appointment type: ", Appointment.Type.class);
    }

    private Appointment getSelectedAppointment(ArrayList<Appointment> list) {
        return InputManager.getInstance().getSelection("Select an appointment to edit or delete: ", list);
    }
}
