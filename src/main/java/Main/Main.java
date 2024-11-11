package Main;

import Email.TestEmail;
import Encryption.AESEncryption;
import Singletons.InputManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) {
        passwordEncryptionTest();
        emailTest();
        Controller.UserSessionController.getInstance().showLoginMenu();
    }

    private static void emailTest() {
        try {
            String email = InputManager.getInstance().getString("Enter email:");
            TestEmail.SendTestMail(email);
            System.out.println("Email sent successfully");
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private static void passwordEncryptionTest() {
        String password = "password";
        String salt = "salt";
        String encrypted = AESEncryption.encrypt(password, "secret", salt);
        System.out.println("Encrypted: " + encrypted);
        String decrypted = AESEncryption.decrypt(encrypted, "secret", salt);
        System.out.println("Decrypted: " + decrypted);
    }

}
