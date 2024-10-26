import CustomTypes.Gender;
import DataHandling.SaveManager;
import Model.Appointment;
import Singletons.AppointmentManager;
import Controller.Controller;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        createDummyAppointments();
        createDummyAvailableDates();

        Controller.getInstance().showLoginMenu();
    }

    private static void createDummyAvailableDates() {
        AppointmentManager.getInstance().updateAvailableDates();
    }

    private static void createDummyAppointments() {
        AppointmentManager.getInstance().add(new Appointment("1", new Date(2323223232L), Appointment.Type.FOLLOWUP));
        AppointmentManager.getInstance().add(new Appointment("2", new Date(2323223232L), Appointment.Type.EMERGENCY));
    }

}
