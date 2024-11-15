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
import View.*;

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
}
