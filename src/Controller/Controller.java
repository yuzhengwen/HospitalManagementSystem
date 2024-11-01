package Controller;

import CustomTypes.OperationMode;
import CustomTypes.Role;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.Patient;
import Model.ScheduleManagement.Schedule;
import Model.ScheduleManagement.TimeSlot;
import Model.Staff;
import Model.User;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import Singletons.UserLoginManager;
import View.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Controller {
    private static Controller instance;
    private final SaveManager saveManager = new SaveManager();

    private Controller() {
        saveManager.loadPatients();
        saveManager.loadStaffs();

        // Create a test schedule for a doctor
        Staff testDoctor = (Staff) UserLoginManager.getInstance().getUserById("D001");

        Schedule schedule = new Schedule(testDoctor);
        schedule.setWorkingHours(DayOfWeek.MONDAY, 8, 20);
        schedule.setWorkingHours(DayOfWeek.TUESDAY, 10, 12);
        schedule.setWorkingHours(DayOfWeek.WEDNESDAY, 8, 17);
        schedule.setWorkingHours(DayOfWeek.THURSDAY, 8, 17);
        schedule.setWorkingHours(DayOfWeek.FRIDAY, 8, 17);

        AppointmentManager.getInstance().setSchedule(testDoctor, schedule);

        // Create test appointment requests
        AppointmentManager.getInstance().add(new Appointment("P1001", LocalDate.of(2024, 11, 4), new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)), Appointment.Type.CHECKUP));
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

    public void showLoginMenu() {
        LoginView loginView = new LoginView();
        loginView.display(); // display the login menu and let user login
        if (loginView.getUser() != null) { // if successful login
            setCurrentUser(loginView.getUser());
            startMainMenu(); // start main menu for the user
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public boolean changePassword(String newPassword) {
        if (currentUser != null) {
            currentUser.changePassword(newPassword);
            saveManager.savePatients();
            return true;
        }
        return false;
    }

    public void logout() {
        saveManager.savePatients();
        saveManager.saveStaffs();
        saveManager.saveAppointments();
        setCurrentUser(null);
        showLoginMenu();
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
        EnumView<Appointment.Type> aptTypeView = new EnumView<>(Appointment.Type.class);
        aptTypeView.display();
        return aptTypeView.getSelected();
    }

    private Appointment getSelectedAppointment(ArrayList<Appointment> list) {
        SelectionView<Appointment> view = new SelectionView<>(list);
        view.display();
        return view.getSelected();
    }
}
