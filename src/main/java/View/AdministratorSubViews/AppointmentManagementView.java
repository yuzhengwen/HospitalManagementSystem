package View.AdministratorSubViews;

import DataHandling.SaveManager;
import Model.Appointment;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import View.Action;
import View.ViewObject;

import java.util.List;

public class AppointmentManagementView extends ViewObject {
    public AppointmentManagementView() {
        actions.add(new Action("Back to Main", () -> 0));
        actions.add(new Action("View Appointments", this::viewAppointments));
    }
    @Override
    public void display() {
        do {
            System.out.println("Appointment Management Menu");
            System.out.println("----------------");
            printActions();
        } while (getInput() != 0);
        SaveManager.getInstance().saveAppointments();
    }
    private int viewAppointments() {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments accepted yet");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments);
            System.out.println(selected.getFullDetails());
        }
        return InputManager.getInstance().goBackPrompt();
    }
}
