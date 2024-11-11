package View;

import Controller.Controller;
import CustomTypes.OperationMode;
import Model.Appointment;
import Model.Patient;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.util.List;

public class PatientView extends UserView {
    Patient patient;

    public PatientView(Patient patient) { // constructor
        super(patient);
        this.patient = patient;
        System.out.println("Welcome, " + patient.getName());
        actions.add(new Action("View Patient Info", this::viewPatientInfo));
        actions.add(new Action("View Appointments", this::viewAppointments));
        actions.add(new Action("Schedule Appointments", this::scheduleAppointments));
        actions.add(new Action("Reschedule Appointments", this::rescheduleAppointments));
        actions.add(new Action("Cancel Appointments", this::cancelAppointments));
    }

    private void viewPatientInfo() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Patient Info:");
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDob());
        System.out.println("Gender: " + patient.getGender());
        InputManager.getInstance().goBackPrompt();
    }

    private void viewAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByPatientId(patient.getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointments found");
        } else {
            System.out.println("Appointments:");
            for (Appointment appointment : appointments)
                System.out.println(appointment.toString());
        }
        InputManager.getInstance().goBackPrompt();
    }

    @Override
    public void display() { // display patient menu
        System.out.println("Patient Menu");
        System.out.println("----------------");
        printActions(); // print list of available actions
        getInput(); // get user input and handle it
    }

    private void scheduleAppointments() {
        Controller.getInstance().setPreviousView(this);

        Appointment newAppointment = Controller.getInstance().manageAppointments(OperationMode.SCHEDULE);
        if (newAppointment != null) {
            System.out.println("Appointment scheduled successfully");
            System.out.println(newAppointment.toString());
        } else
            System.out.println("Appointment scheduling failed");

        InputManager.getInstance().goBackPrompt();
    }

    private void rescheduleAppointments() {
        Controller.getInstance().setPreviousView(this);

        System.out.println("Choose an appointment to reschedule: ");
        Appointment appointment = Controller.getInstance().manageAppointments(OperationMode.EDIT);
        if (appointment != null) {
            System.out.println("Appointment rescheduled successfully");
            System.out.println(appointment.toString());
        } else
            System.out.println("Appointment rescheduling failed");

        InputManager.getInstance().goBackPrompt();
    }

    private void cancelAppointments() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Choose an appointment to cancel: ");
        if (Controller.getInstance().manageAppointments(OperationMode.DELETE) != null)
            System.out.println("Appointment cancelled successfully");
        else
            System.out.println("Appointment cancellation failed");
        InputManager.getInstance().goBackPrompt();
    }
}
