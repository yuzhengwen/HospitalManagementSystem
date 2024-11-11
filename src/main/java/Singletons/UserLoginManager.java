package Singletons;

import Model.Patient;
import Model.Staff;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserLoginManager {
    private ArrayList<User> users = new ArrayList<>();

    private static UserLoginManager instance;

    private UserLoginManager() {
    }

    public static synchronized UserLoginManager getInstance() {
        if (instance == null) { // if instance is null, create a new instance
            instance = new UserLoginManager();
        }
        return instance;
    }

    public User getUserById(String id) {
        for (User user : users) { // iterate through all users to find the user with the given ID
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null; // return null if user is not found
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Patient) {
                patients.add((Patient) user);
            }
        }
        return patients;
    }

    public List<Staff> getAllStaffs() {
        List<Staff> staffs = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Staff) {
                staffs.add((Staff) user);
            }
        }
        return staffs;
    }


    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
