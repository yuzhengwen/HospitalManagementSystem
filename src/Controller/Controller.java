package Controller;

import CustomTypes.OperationMode;
import CustomTypes.Role;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.Patient;
import Model.Staff;
import Model.User;
import Singletons.AppointmentManager;
import View.*;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private static Controller instance;
    private final SaveManager saveManager = new SaveManager();

    private Controller() {
        saveManager.loadPatients();
        saveManager.loadStaffs();
        saveManager.loadAppointments();
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
                new DoctorView((Staff)currentUser).display(); // display the doctor menu
            }
            if (role == Role.ADMINISTRATOR){
                //new AdminView(currentUser).display();
            }
            if (role == Role.PHARMACIST){
                //new PharmacistView(currentUser).display();
            }
        }
    }

    public boolean showAppointments(OperationMode mode) {
        ArrayList<Appointment> list = AppointmentManager.getInstance().getAppointments();
        if (currentUser instanceof Patient) {
            list = AppointmentManager.getInstance().getAppointmentsByPatientId((currentUser.getId()));

            if (mode == OperationMode.SCHEDULE) {
                // show view to select date from available dates
                Date selectedDate = getSelectedDate(AppointmentManager.getInstance().getAvailableDates());
                if (selectedDate != null) {
                    // Get available doctors for the selected date
                    ArrayList<Staff> availableDoctors = AppointmentManager.getInstance().getAvailableDoctors(selectedDate);
                    if (!availableDoctors.isEmpty()) { // if there are available doctors
                        // Prompt user to select a doctor
                        Staff selectedDoctor = getSelectedDoctor(availableDoctors);
                        if (selectedDoctor != null) {
                            EnumView<Appointment.Type> aptTypeView = new EnumView<>(Appointment.Type.class);
                            aptTypeView.display();
                            AppointmentManager.getInstance().add(new Appointment(selectedDoctor.getId(), selectedDate, currentUser.getId(), aptTypeView.getSelected(), Appointment.Status.PENDING));
                            return true;
                        } else {
                            System.out.println("No doctor selected.");
                        }
                    } else {
                        System.out.println("No doctors available on the selected date.");
                    }
                }
            } else if (mode == OperationMode.EDIT) {
                // show view to select appointment from patient's appointments
                Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
                if (selected != null) {
                    Date selectedDate = getSelectedDate(AppointmentManager.getInstance().getAvailableDates());
                    if (selectedDate != null) {
                        selected.setDate(selectedDate);
                        return true;
                    }
                }
            } else if (mode == OperationMode.DELETE) {
                // show view to select appointment from patient's appointments
                Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
                if (selected != null) {
                    AppointmentManager.getInstance().remove(selected);
                    return true;
                }
            }
        }
        return false;
    }
    private Staff getSelectedDoctor(ArrayList<Staff> list) {
        System.out.println("Choose doctor: ");
        SelectionView<Staff> view = new SelectionView<>(list);
        view.display();
        return view.getSelected();
    }

    private Date getSelectedDate(ArrayList<Date> list) {
        System.out.println("Choose date: ");
        SelectionView<Date> view = new SelectionView<>(list);
        view.display();
        return view.getSelected();
    }

    private Appointment getSelectedAppointment(ArrayList<Appointment> list) {
        SelectionView<Appointment> view = new SelectionView<>(list);
        view.display();
        return view.getSelected();
    }
}
