import Controller.UserSessionController;
import Singletons.InputManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) {
        try {
            //String toEmail = "marcuslimlj@gmail.com";
            //String toEmail = "dexterteo4@gmail.com";
            String email = InputManager.getInstance().getString("Enter email:");
            TestEmail.SendTestMail(email);
            System.out.println("Email sent successfully");
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
        Controller.UserSessionController.getInstance().showLoginMenu();
    }

}
