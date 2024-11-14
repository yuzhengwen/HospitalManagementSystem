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
    public static boolean passwordValidation = true;

    public LoginView() {
        actions.add(new Action("Login", this::login));
        actions.add(new Action("Create Account", this::createAccount));
        actions.add(new Action("Toggle Password Validation (For Testing)", this::toggleValidation));
        actions.add(new Action("Exit", () -> {
            System.exit(0);
            return 0;
        }));
    }

    public void display() { // display the login menu
        do {
            System.out.println("Welcome to the Hospital Management System");
            System.out.println("------------------------------------------");
            printActions(); // print list of available actions
        } while (getInput() != 0);
    }

    private int login() {
        System.out.println("Login Menu");
        System.out.println("----------");
        user = getLoginRecursive();
        return 0;
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

    private int toggleValidation() {
        passwordValidation = !passwordValidation;
        System.out.println("Password validation is now " + (passwordValidation ? "enabled" : "disabled"));
        return 1;
    }

    public int createAccount() {
        System.out.println("Create Account");
        System.out.println("--------------");
        String id;
        do {
            id = InputManager.getInstance().getString("Enter ID: ");
        } while (UserLoginManager.getInstance().getUserById(id) != null); // id alr exists
        String password = InputManager.getInstance().getNewPasswordInput(passwordValidation);
        Role role = InputManager.getInstance().getEnum("Choose role: ", Role.class);
        String name = InputManager.getInstance().getString("Enter name: ");
        Gender gender = InputManager.getInstance().getEnum("Choose gender: ", Gender.class);
        switch (role) {
            case Role.PATIENT:
                LocalDate dob = InputManager.getInstance().getDate("Enter Date of Birth: (dd-MM-yyyy)");
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
        System.out.println("Account created successfully");
        return 1;
    }

}
