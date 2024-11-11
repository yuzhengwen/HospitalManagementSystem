package View;

import CustomTypes.Gender;
import CustomTypes.Role;
import Model.Patient;
import Model.Staff;
import Model.User;
import Singletons.InputManager;
import Singletons.UserLoginManager;

import java.time.LocalDate;

public class LoginView extends ViewObject {
    private User user;

    public LoginView() {
        actions.add(new Action("Login", this::login));
        actions.add(new Action("Create Account", this::createAccount));
    }

    public void display() { // display the login menu
        System.out.println("Welcome to the Hospital Management System");
        System.out.println("------------------------------------------");
        printActions(); // print list of available actions
        getInput(); // get user input and handle it
    }

    private void login() {
        System.out.println("Login Menu");
        System.out.println("----------");
        user = getLoginRecursive();
    }

    private User getLoginRecursive() {
        User user;
        do {
            String id = InputManager.getInstance().getString("Enter ID: "); // request for ID
            user = UserLoginManager.getInstance().getUserById(id); // check if the ID exists
            if (user == null) {
                System.out.println("ID not found. Please try again.");
            }
        } while (user == null); // repeat until correct ID is entered

        String password = InputManager.getInstance().getString("Enter Password: "); // request for password
        if (user.getPassword().equals(password)) { // check if password matches
            return user;
        } else { // if password does not match
            System.out.println("Incorrect password or password does not match. Please try again.");
            return getLoginRecursive(); // recursive call - prompt for ID and password again
        }
    }

    public User getUser() {
        return user;
    }

    public void createAccount() {
        System.out.println("Create Account");
        System.out.println("--------------");
        String id, password, confirmPassword;
        do {
            id = InputManager.getInstance().getString("Enter ID: ");
        } while (UserLoginManager.getInstance().getUserById(id) != null); // id alr exists
        do {
            password = InputManager.getInstance().getString("Enter Password: ");
        } while (!validatePassword(password));
        do {
            confirmPassword = InputManager.getInstance().getString("Confirm Password: ");
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
            }
        } while (!password.equals(confirmPassword));
        Role role = InputManager.getInstance().getEnum("Choose role: ", Role.class);
        String name = InputManager.getInstance().getString("Enter name: ");
        Gender gender = InputManager.getInstance().getEnum("Choose gender: ", Gender.class);
        switch (role) {
            case Role.PATIENT:
                LocalDate dob = InputManager.getInstance().getDate("Enter Date of Birth: ");
                String bloodType = InputManager.getInstance().getString("Enter blood type: ");
                String email = InputManager.getInstance().getString("Enter email: ");
                user = new Patient(id, password, name, dob, bloodType, gender);
                break;
            case Role.DOCTOR:
                int age = InputManager.getInstance().getInt("Enter age: ");
                user = new Staff(id, password, name, role, gender, age);
                break;
        }
        UserLoginManager.getInstance().addUser(user);
        display();
    }

    /***
     * Validate password
     * @param password password to validate
     * @return true if password is valid, false otherwise
     */
    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            System.out.println("Password must contain at least one digit.");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        if (!password.matches(".*[!@#$%^&*].*")) {
            System.out.println("Password must contain at least one special character.");
            return false;
        }
        if (password.contains(" ")) {
            System.out.println("Password must not contain spaces.");
            return false;
        }
        if (password.trim().equalsIgnoreCase("password")) {
            System.out.println("Password cannot be 'password'.");
            return false;
        }
        return true;
    }
}
