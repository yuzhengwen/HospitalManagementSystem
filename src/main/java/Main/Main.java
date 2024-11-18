package Main;

import DataHandling.SaveManager;
import Email.GmailSender;
import GoogleBucket.UploadBucket;
import Singletons.InputManager;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) {
        SaveManager.getInstance().loadAllData();
        Controller.UserSessionController.getInstance().showLoginMenu();
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
