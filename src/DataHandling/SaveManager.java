package DataHandling;

import Model.Appointment;
import Model.Patient;
import Singletons.AppointmentManager;
import Singletons.UserLoginManager;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private final ISaveService saveService = new LocalFileHandler();
    private final PatientSerializer patientSerializer = new PatientSerializer();

    private final String FOLDER_PATH = "./src/SaveData/";
    private final String PATIENT_FILE = "patients.txt";
    private final String APPOINTMENT_FILE = "appointments.txt";

    private final String INITIAL_PATIENTS = "./src/CSV/Patient_List.csv";
    private final String INITIAL_STAFF = "./src/CSV/Staff_List.csv";
    private final String INITIAL_MEDICINE = "./src/CSV/Medicine_List.csv";

    public void savePatients() {
        List<Patient> patients = UserLoginManager.getInstance().getAllPatients();
        List<String> serializedPatients = new ArrayList<>();
        for (Patient patient : patients) {
            serializedPatients.add(patientSerializer.serialize(patient));
        }
        saveService.saveData(PATIENT_FILE, serializedPatients);
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
        for (String serializedPatient : serializedPatients) {
            Patient p = patientSerializer.deserialize(serializedPatient);
            UserLoginManager.getInstance().addUser(p);
        }
    }
    public void initialLoad(){
        List<String> serializedPatients = saveService.readData(INITIAL_PATIENTS);
        serializedPatients.removeFirst(); // remove the header
        for (String serializedPatient : serializedPatients) {
            Patient p = patientSerializer.deserialize(serializedPatient);
            UserLoginManager.getInstance().addUser(p);
        }
    }
}
