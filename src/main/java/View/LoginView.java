package View;

import Model.User;
import Singletons.InputManager;
import Singletons.UserLoginManager;

public class LoginView implements IView {
    private User user;

    public void display() { // display the login menu
        System.out.println("Login Menu");
        System.out.println("----------");
        user = getLoginRecursive(); // get user login information
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
}
