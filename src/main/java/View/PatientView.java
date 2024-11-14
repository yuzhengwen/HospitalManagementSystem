package View;

import Controller.Controller;
import CustomTypes.ContactInfo;
import CustomTypes.OperationMode;
import Model.Appointment;
import Model.Patient;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.util.List;

public class PatientView extends UserView<Patient> {
    public PatientView(Patient patient) { // constructor
        super(patient);
        System.out.println("Welcome, " + patient.getName());
        actions.add(new Action("View Medical Record", this::viewPatientInfo));
        actions.add(new Action("Add/Update Contact Info", this::updateContactInfo));
        actions.add(new Action("View Appointments", this::viewAppointments));
        actions.add(new Action("Schedule Appointments", this::scheduleAppointments));
        actions.add(new Action("Reschedule Appointments", this::rescheduleAppointments));
        actions.add(new Action("Cancel Appointments", this::cancelAppointments));
    }

    @Override
    public void display() { // display patient menu
        do {
            System.out.println("Patient Menu");
            System.out.println("----------------");
            printActions(); // print list of available actions
        } while (getInput() != 0);
    }

    private int updateContactInfo() {
        Controller.getInstance().setPreviousView(this);
        // get reference to the patient's contact info
        ContactInfo contactInfo = user.getContactInfo();
        System.out.println("Current Contact Info:");
        System.out.println(contactInfo.toString());
        System.out.println("Enter new contact info:");
        String phone = InputManager.getInstance().getString("Phone:");
        String email = InputManager.getInstance().getString("Email:");
        contactInfo.phoneNumber = phone;
        contactInfo.email = email;
        System.out.println("Contact info updated successfully");
        System.out.println(contactInfo.toString());
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewPatientInfo() {
        Controller.getInstance().setPreviousView(this);
        System.out.println(user.getMedicalRecord());
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByPatientId(user.getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointments found");
        } else {
            System.out.println("Appointments:");
            for (Appointment appointment : appointments)
                System.out.println(appointment.toString());
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int scheduleAppointments() {
        Controller.getInstance().setPreviousView(this);

        Appointment newAppointment = Controller.getInstance().manageAppointments(OperationMode.SCHEDULE);
        if (newAppointment != null) {
            System.out.println("Appointment scheduled successfully");
            System.out.println(newAppointment.toString());
        } else
            System.out.println("Appointment scheduling failed");

        return InputManager.getInstance().goBackPrompt();
    }

    private int rescheduleAppointments() {
        Controller.getInstance().setPreviousView(this);

        System.out.println("Choose an appointment to reschedule: ");
        Appointment appointment = Controller.getInstance().manageAppointments(OperationMode.EDIT);
        if (appointment != null) {
            System.out.println("Appointment rescheduled successfully");
            System.out.println(appointment.toString());
        } else
            System.out.println("Appointment rescheduling failed");

        return InputManager.getInstance().goBackPrompt();
    }

    private int cancelAppointments() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Choose an appointment to cancel: ");
        if (Controller.getInstance().manageAppointments(OperationMode.DELETE) != null)
            System.out.println("Appointment cancelled successfully");
        else
            System.out.println("Appointment cancellation failed");
        return InputManager.getInstance().goBackPrompt();
    }
}
