package View;

import Model.User;
import Singletons.InputManager;
import Singletons.UserLoginManager;

public class LoginView implements IView {
    private User user;

    public void display() {
        System.out.println("Login Menu");
        System.out.println("----------");
        user = getLoginRecursive();
    }

    private User getLoginRecursive() {
        User user;
        do {
            String id = InputManager.getInstance().getString("Enter ID: ");
            user = UserLoginManager.getInstance().getUserById(id);
            if (user == null) {
                System.out.println("ID not found. Please try again.");
            }
        } while (user == null);

        String password = InputManager.getInstance().getString("Enter Password: ");
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            System.out.println("Incorrect password or password does not match. Please try again.");
            return getLoginRecursive();
        }
    }

    public User getUser() {
        return user;
    }
}
