package DataHandling;

import Model.Appointment;
import Model.Patient;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.UserLoginManager;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private final ISaveService saveService = new LocalFileHandler();
    private final PatientSerializer patientSerializer = new PatientSerializer();
    private final StaffSerializer staffSerializer = new StaffSerializer();
    private final AppointmentSerializer appointmentSerializer = new AppointmentSerializer();

    private final String FOLDER_PATH = "./src/SaveData/";
    private final String APPOINTMENT_FILE = "./src/CSV/Appointment_List.csv";

    private final String PATIENT_FILE = "./src/CSV/Patient_List.csv";
    private final String STAFF_FILE = "./src/CSV/Staff_List.csv";
    private final String INITIAL_MEDICINE = "./src/CSV/Medicine_List.csv";

    public void savePatients() {
        List<Patient> patients = UserLoginManager.getInstance().getAllPatients();
        List<String> stringsToWrite = new ArrayList<>();
        stringsToWrite.add("Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information,Password");
        for (Patient patient : patients) {
            stringsToWrite.add(patientSerializer.serialize(patient));
        }
        saveService.saveData(PATIENT_FILE, stringsToWrite);
    }

    public void saveStaffs() {
        List<Staff> staffs = UserLoginManager.getInstance().getAllStaffs();
        List<String> stringsToWrite = new ArrayList<>();
        stringsToWrite.add("Staff ID,Name,Role,Gender,Age,Password");
        for (Staff staff : staffs) {
            stringsToWrite.add(staffSerializer.serialize(staff));
        }
        saveService.saveData(STAFF_FILE, stringsToWrite);
    }
    public void saveAppointments() {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointments();
        List<String> stringsToWrite = new ArrayList<>();
        for (Appointment appointment : appointments) {
            stringsToWrite.add(appointmentSerializer.serialize(appointment));
        }
        saveService.saveData(APPOINTMENT_FILE, stringsToWrite);
    }
    /*
    TODO: Implement saveAppointments
    public void saveAppointments() {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointments();
        List<String> serializedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            serializedAppointments.add(appointment.serialize());
        }
        saveService.saveData(APPOINTMENT_FILE, serializedAppointments);
    }*/

    public void loadPatients() {
        List<String> serializedPatients = saveService.readData(PATIENT_FILE);
        serializedPatients.remove(0); // remove the header
        for (String serializedPatient : serializedPatients) {
            Patient p = patientSerializer.deserialize(serializedPatient);
            UserLoginManager.getInstance().addUser(p);
        }
    }
    public void loadStaffs() {
        List<String> serializedStaffs = saveService.readData(STAFF_FILE);
        serializedStaffs.remove(0); // remove the header
        for (String serializedStaff : serializedStaffs) {
            Staff s = staffSerializer.deserialize(serializedStaff);
            UserLoginManager.getInstance().addUser(s);
        }
    }
    public void loadAppointments() {
        List<String> serializedAppointments = saveService.readData(APPOINTMENT_FILE);
        for (String serializedAppointment : serializedAppointments) {
            Appointment appointment = appointmentSerializer.deserialize(serializedAppointment);
            AppointmentManager.getInstance().addAppointment(appointment);
        }
    }
}
