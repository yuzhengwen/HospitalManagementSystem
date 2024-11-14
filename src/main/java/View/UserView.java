package View;

import Controller.UserSessionController;
import Model.User;
import Singletons.InputManager;

public abstract class UserView<T extends User> extends ViewObject {
    protected final T user;

    protected UserView(T user) {
        this.user = user;
        actions.add(new Action("Change Password", this::changePassword));
        actions.add(new Action("Logout", this::logout));
    }

    private int changePassword() {
        String newPassword = InputManager.getInstance().getNewPasswordInput(LoginView.passwordValidation);
        if (UserSessionController.getInstance().changePassword(newPassword)) {
            System.out.println("Password changed successfully");
        } else {
            System.out.println("Password change failed");
        }
        System.out.println();
        return 1;
    }

    private int logout() {
        System.out.println("Logging out...");
        System.out.println();
        UserSessionController.getInstance().logout();
        return 0;
    }
}
