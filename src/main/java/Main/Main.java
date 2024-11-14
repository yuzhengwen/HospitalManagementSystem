package Main;

import DataHandling.SaveManager;
import Email.TestEmail;
import GoogleBucket.UploadBucket;
import Singletons.InputManager;

import java.io.*;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) {
        //uploadTest();
        //emailTest();

        SaveManager.getInstance().loadAllData();
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
