package View;

import Controller.Controller;
import Model.User;
import Singletons.InputManager;

import java.util.Scanner;

public abstract class UserView extends ViewObject {
    public User user;

    public UserView(User user) {
        this.user = user;
        actions.add(new Action("Change Password", this::changePassword));
        actions.add(new Action("Logout", this::logout));
    }

    private void changePassword() {
        String newPassword = InputManager.getInstance().getString("Enter new password: ");
        if (Controller.getInstance().changePassword(newPassword)) {
            System.out.println("Password changed successfully");
        } else {
            System.out.println("Password change failed");
        }
        System.out.println();
        display();
    }

    private void logout() {
        System.out.println("Logging out...");
        System.out.println();
        Controller.getInstance().logout();
    }
}
