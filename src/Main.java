import CustomTypes.Gender;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.User;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Singletons.UserLoginManager;
import Controller.Controller;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SaveManager saveManager = new SaveManager();
        saveManager.initialLoad(); // load initial data
        saveManager.savePatients(); // to test the save functionality

        createDummyAppointments();
        createDummyAvailableDates();

        if (showLoginMenu())
            Controller.getInstance().startMainMenu();
    }

    private static void createDummyAvailableDates() {
        AppointmentManager.getInstance().updateAvailableDates();
    }

    private static void createDummyAppointments() {
        AppointmentManager.getInstance().add(new Appointment("1", new Date(2323223232L), Appointment.Type.FOLLOWUP));
        AppointmentManager.getInstance().add(new Appointment("2", new Date(2323223232L), Appointment.Type.EMERGENCY));
    }

    private static boolean showLoginMenu() {
        String id = InputManager.getInstance().getString("Enter ID: ");
        User user = UserLoginManager.getInstance().getUserById(id);
        String password = InputManager.getInstance().getString("Enter Password: ");
        if (user != null && user.getPassword().equals(password)) {
            Controller.getInstance().setCurrentUser(user);
            return true;
        }
        return false;
    }
}
