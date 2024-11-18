package View;

import java.time.LocalDate;
import java.util.List;

import Controller.Controller;
import CustomTypes.OperationMode;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.Patient;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import Model.Staff;
import Singletons.AppointmentFilter;
import Singletons.AppointmentManager;
import Singletons.InputManager;

public class PatientView extends UserView<Patient> {
    public PatientView(Patient patient) { // constructor
        super(patient);
        System.out.println("Welcome, " + patient.getName());
        actions.add(new Action("View Medical Record", this::viewPatientInfo));
        actions.add(new Action("Add/Update Contact Info", this::updateContactInfo));
        actions.add(new Action("View Completed Appointments", this::viewCompletedAppointments));
        actions.add(new Action("View Scheduled Appointments", this::viewScheduledAppointments));
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
        System.out.println("Current Contact Info:");
        System.out.println(user.getContactInfo().toString());
        System.out.println("Enter new contact info:");
        String phone = InputManager.getInstance().getString("Phone:");
        String email = InputManager.getInstance().getString("Email:");
        user.setContactInfo(phone, email);
        System.out.println("Contact info updated successfully");
        System.out.println(user.getContactInfo().toString());
        SaveManager.getInstance().savePatients();
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewPatientInfo() {
        Controller.getInstance().setPreviousView(this);
        System.out.println(user.getMedicalRecord());
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewCompletedAppointments() {
        Controller.getInstance().setPreviousView(this);
        AppointmentFilter filter = new AppointmentFilter().filterByPatient(user.getId())
                .filterByStatus(Appointment.Status.COMPLETED);
        return viewAppointments(filter);
    }

    private int viewScheduledAppointments() {
        Controller.getInstance().setPreviousView(this);
        AppointmentFilter filter = new AppointmentFilter().filterByPatient(user.getId())
                .filterByStatus(Appointment.Status.ACCEPTED)
                .filterByStatus(Appointment.Status.PENDING);
        return viewAppointments(filter);
    }

    private int viewAppointments(AppointmentFilter filter) {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsWithFilter(filter);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments);
            if (selected != null)
                System.out.println(selected.getFullDetails());
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int scheduleAppointments() {
        Controller.getInstance().setPreviousView(this);

        Appointment newAppointment = manageAppointments(OperationMode.SCHEDULE);
        if (newAppointment != null) {
            System.out.println("Appointment scheduled successfully");
            System.out.println(newAppointment.toString());
        } else
            System.out.println("Appointment scheduling failed");

        SaveManager.getInstance().saveAppointments();
        return InputManager.getInstance().goBackPrompt();
    }

    private int rescheduleAppointments() {
        Controller.getInstance().setPreviousView(this);

        System.out.println("Choose an appointment to reschedule: ");
        Appointment appointment = manageAppointments(OperationMode.EDIT);
        if (appointment != null) {
            System.out.println("Appointment rescheduled successfully");
            System.out.println(appointment.toString());
        } else
            System.out.println("Appointment rescheduling failed");

        SaveManager.getInstance().saveAppointments();
        return InputManager.getInstance().goBackPrompt();
    }

    private int cancelAppointments() {
        Controller.getInstance().setPreviousView(this);
        System.out.println("Choose an appointment to cancel: ");
        if (manageAppointments(OperationMode.DELETE) != null)
            System.out.println("Appointment cancelled successfully");
        else
            System.out.println("Appointment cancellation failed");
        SaveManager.getInstance().saveAppointments();
        return InputManager.getInstance().goBackPrompt();
    }

    private Appointment manageAppointments(OperationMode mode) {
        if (OperationMode.SCHEDULE == mode) { // schedule new appointment
            // select date, timeslot, doctor, type
            LocalDate date;
            TimeSlotWithDoctor timeSlot;
            do {
                date = InputManager.getInstance().getDate();
                timeSlot = InputManager.getInstance().selectTimeSlot(date, user.getId());
            } while (timeSlot == null);
            List<Staff> doctors = timeSlot.getAvailableDoctors();
            Staff selectedDoctor = InputManager.getInstance().getSelection("Select a doctor: ", doctors);
            Appointment.Type type = InputManager.getInstance().getEnum("Select appointment type: ", Appointment.Type.class);

            // create the appointment object and add it to the list
            Appointment newAppointment = new Appointment(user.getId(), date, timeSlot.getTimeSlot(), type);
            newAppointment.setDoctorId(selectedDoctor.getId());
            AppointmentManager.getInstance().add(newAppointment);
            return newAppointment;
        } else if (OperationMode.EDIT == mode) { // reschedule existing appointment
            AppointmentFilter filter = new AppointmentFilter().filterByPatient(user.getId())
                    .filterByStatus(Appointment.Status.ACCEPTED)
                    .filterByStatus(Appointment.Status.PENDING); // filter and get user to select appointment to edit
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsWithFilter(filter));
            if (selected != null) {
                LocalDate date;
                TimeSlotWithDoctor timeSlot;
                do {
                    date = InputManager.getInstance().getDate();
                    timeSlot = InputManager.getInstance().selectTimeSlot(date, user.getId());
                } while (timeSlot == null);
                List<Staff> doctors = timeSlot.getAvailableDoctors();
                Staff selectedDoctor = InputManager.getInstance().getSelection("Select a doctor: ", doctors);
                selected.setDate(date);
                selected.setTimeSlot(timeSlot.getTimeSlot());
                selected.setDoctorId(selectedDoctor.getId());
                selected.setStatus(Appointment.Status.PENDING);
                return selected;
            }
        } else if (OperationMode.DELETE == mode) { // cancel existing appointment
            AppointmentFilter filter = new AppointmentFilter().filterByPatient(user.getId())
                    .filterByStatus(Appointment.Status.ACCEPTED)
                    .filterByStatus(Appointment.Status.PENDING); // filter only accepted and pending appointments
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsWithFilter(filter));
            if (selected != null) {
                AppointmentManager.getInstance().declineAppointment(selected); // mark as cancelled
                return selected;
            }
        }
        return null; // return null if no appointment was selected
    }

    private Appointment getSelectedAppointment(List<Appointment> list) {
        return InputManager.getInstance().getSelection("Select an appointment to edit or delete: ", list);
    }
}
