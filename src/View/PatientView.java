package View;

import Controller.Controller;
import CustomTypes.OperationMode;
import Model.Patient;
import Model.ScheduleManagement.TimeSlot;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PatientView extends UserView {
    Patient patient;

    public PatientView(Patient patient) { // constructor
        super(patient);
        this.patient = patient;
        System.out.println("Welcome, " + patient.getName());
        actions.add(new Action("View Available Slots by Day", this::viewAvailableSlotsByDay));
        actions.add(new Action("Schedule Appointments", this::scheduleAppointments));
        actions.add(new Action("Reschedule Appointments", this::rescheduleAppointments));
        actions.add(new Action("Cancel Appointments", this::cancelAppointments));
    }

    @Override
    public void display() { // display patient menu
        System.out.println("Patient Menu");
        System.out.println("----------------");
        printActions(); // print list of available actions
        getInput(); // get user input and handle it
    }
    private void viewAvailableSlotsByDay() {
        Controller.getInstance().setPreviousView(this);

        LocalDate date = InputManager.getInstance().getDate();
        Controller.getInstance().scheduleAppointmentByDayView(date);

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
