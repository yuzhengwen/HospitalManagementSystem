package View;

import Controller.Controller;
import CustomTypes.ContactInfo;
import CustomTypes.OperationMode;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.Patient;
import Model.ScheduleManagement.TimeSlot;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.time.LocalDate;
import java.util.ArrayList;
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
        SaveManager.getInstance().savePatients();
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
        if (OperationMode.SCHEDULE == mode) {
            // select date, timeslot, doctor, type
            LocalDate date = InputManager.getInstance().getDate();
            TimeSlotWithDoctor timeSlot = selectTimeSlot(date);
            List<Staff> doctors = timeSlot.getDoctors();
            Staff selectedDoctor = InputManager.getInstance().getSelection("Select a doctor: ", doctors);
            Appointment.Type type = selectAppointmentType();

            // create the appointment object and add it to the list
            Appointment newAppointment = new Appointment(user.getId(), date, timeSlot.getTimeSlot(), type);
            newAppointment.setDoctorId(selectedDoctor.getId());
            AppointmentManager.getInstance().add(newAppointment);
            return newAppointment;
        } else if (OperationMode.EDIT == mode) {
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(user.getId()));
            if (selected != null) {
                LocalDate date = InputManager.getInstance().getDate();
                TimeSlotWithDoctor timeSlot = selectTimeSlot(date);
                List<Staff> doctors = timeSlot.getDoctors();
                Staff selectedDoctor = InputManager.getInstance().getSelection("Select a doctor: ", doctors);
                selected.setDate(date);
                selected.setTimeSlot(timeSlot.getTimeSlot());
                selected.setDoctorId(selectedDoctor.getId());
                return selected;
            }
        } else if (OperationMode.DELETE == mode) {
            Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(user.getId()));
            if (selected != null) {
                AppointmentManager.getInstance().remove(selected);
                return selected;
            }
        }
        return null;
    }

    private TimeSlotWithDoctor selectTimeSlot(LocalDate date) {
        // get timeslots with doctors for the selected date
        List<TimeSlotWithDoctor> timeSlotWithDoctors = AppointmentManager.getInstance().getTimeslotWithDoctorList(date);
        // add patientBusy flag to the timeslots that the patient already has an appointment
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByPatientId(user.getId());
        List<TimeSlot> patientBusyTimeSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            patientBusyTimeSlots.add(appointment.getTimeSlot());
        }
        for (TimeSlotWithDoctor timeSlotWithDoctor : timeSlotWithDoctors) {
            if (patientBusyTimeSlots.contains(timeSlotWithDoctor.getTimeSlot())) {
                timeSlotWithDoctor.setPatientBusy(true);
            }
        }
        // display the available timeslots
        SelectionView<TimeSlotWithDoctor> timeSlotSelectionView = new SelectionView<>(timeSlotWithDoctors);
        timeSlotSelectionView.display();
        return timeSlotSelectionView.getSelected();
    }

    private Appointment.Type selectAppointmentType() {
        return InputManager.getInstance().getEnum("Select appointment type: ", Appointment.Type.class);
    }

    private Appointment getSelectedAppointment(List<Appointment> list) {
        return InputManager.getInstance().getSelection("Select an appointment to edit or delete: ", list);
    }
}
