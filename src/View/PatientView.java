package View;

import Controller.Controller;
import CustomTypes.OperationMode;
import Model.Patient;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.util.ArrayList;
import java.util.Date;

public class PatientView extends UserView {
    Patient patient;

    public PatientView(Patient patient) {
        super(patient);
        this.patient = patient;
        System.out.println("Welcome, " + patient.getName());
        actions.add(new Action("View Available Slots", this::viewAvailableSlots));
        actions.add(new Action("Schedule Appointments", this::scheduleAppointments));
        actions.add(new Action("Reschedule Appointments", this::rescheduleAppointments));
        actions.add(new Action("Cancel Appointments", this::cancelAppointments));
    }

    @Override
    public void display() {
        System.out.println("Patient Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }

    private void viewAvailableSlots() {
        Controller.getInstance().setPreviousView(this);
        ArrayList<Date> availableDates = AppointmentManager.getInstance().getAvailableDates();
        availableDates.forEach(date -> System.out.println(date.toString()));
        InputManager.getInstance().getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    private void scheduleAppointments() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Choose an appointment to schedule: ");
        if (Controller.getInstance().showAppointments(OperationMode.SCHEDULE))
            System.out.println("Appointment scheduled successfully");
        else
            System.out.println("Appointment scheduling failed");
        InputManager.getInstance().goBackPrompt();
    }

    private void rescheduleAppointments() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Choose an appointment to reschedule: ");
        if (Controller.getInstance().showAppointments(OperationMode.EDIT))
            System.out.println("Appointment rescheduled successfully");
        else
            System.out.println("Appointment rescheduling failed");
        InputManager.getInstance().goBackPrompt();
    }

    private void cancelAppointments() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Choose an appointment to cancel: ");
        if (Controller.getInstance().showAppointments(OperationMode.DELETE))
            System.out.println("Appointment cancelled successfully");
        else
            System.out.println("Appointment cancellation failed");
        InputManager.getInstance().goBackPrompt();
    }
}
