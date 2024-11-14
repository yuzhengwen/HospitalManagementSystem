package Singletons;

import Model.Patient;
import Model.Staff;
import Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class that manages the list of registered users
 */
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

    /**
     * Get a user by their username
     * @param id the id of the user
     * @return the user with the given username
     */
    public User getUserById(String id) {
        for (User user : users) { // iterate through all users to find the user with the given ID
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null; // return null if user is not found
    }

    /**
     * Get all patients
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Patient) {
                patients.add((Patient) user);
            }
        }
        return patients;
    }

    /**
     * Get all staffs
     * @return a list of all staffs
     */
    public List<Staff> getAllStaffs() {
        List<Staff> staffs = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Staff) {
                staffs.add((Staff) user);
            }
        }
        return staffs;
    }

    /**
     * Add a user to the list of registered users
     * @param user the user to be added
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Remove a user from the list of registered users
     * @param user the user to be removed
     */
    public void removeUser(User user) {
        users.remove(user);
    }

    /**
     * Get the list of registered users
     * @return the list of registered users
     */
    public ArrayList<User> getUsers() {
        return users;
    }
}
