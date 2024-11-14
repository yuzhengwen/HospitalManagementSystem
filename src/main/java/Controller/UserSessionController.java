package Controller;

import DataHandling.SaveManager;
import Model.User;
import Singletons.InputManager;
import Singletons.UserLoginManager;
import View.LoginView;

public class UserSessionController {

    private static UserSessionController instance;
    public static synchronized UserSessionController getInstance() {
        if (instance == null) { // if instance is null, create a new instance
            instance = new UserSessionController();
        }
        return instance;
    }
    private User currentUser;

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void showLoginMenu() {
        LoginView loginView = new LoginView();
        loginView.display(); // display the login menu and let user login
        if (loginView.getUser() != null) { // if successful login
            setCurrentUser(loginView.getUser());
            Controller.getInstance().setCurrentUser(currentUser);
            Controller.getInstance().startMainMenu(); // start main menu for the user
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public boolean changePassword(String newPassword) {
        if (currentUser != null) {
            currentUser.changePassword(newPassword);
            SaveManager.getInstance().savePatients();
            SaveManager.getInstance().saveStaffs();
            return true;
        }
        return false;
    }

    public void logout() {
        SaveManager.getInstance().saveAllData();
        setCurrentUser(null);
        showLoginMenu();
    }
}
