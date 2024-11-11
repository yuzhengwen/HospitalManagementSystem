package Main;

import Email.TestEmail;
import Encryption.AESEncryption;
import GoogleBucket.UploadBucket;
import Singletons.InputManager;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) {
        uploadTest();
        emailTest();
        passwordEncryptionTest();
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

    private static void uploadTest() {
        try {
            File file = createObject();
            UploadBucket.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File createObject() throws IOException {
        File file = new File("TestFile.txt");
        if (file.createNewFile())
            System.out.println("File created: " + file.getName());
        return file;
    }
}
