package GoogleBucket;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.io.*;
import java.nio.file.Paths;

public class UploadBucket {
    private static String projectID = "centering-crow-399215";
    private static String bucketName = "yuzu-hospital-bucket";
    private static final String CREDENTIALS_FILE_PATH = "/service-credentials.json";

    private static GoogleCredentials getCredentials() throws IOException {
        InputStream serviceAccountStream = UploadBucket.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (serviceAccountStream == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        return GoogleCredentials.fromStream(serviceAccountStream);
    }

    private static void uploadFile(String projectID, String bucketName, File file) throws IOException {
        // Upload the file to the specified bucket
        Credentials credentials = getCredentials();
        Storage storage = StorageOptions.newBuilder().setProjectId(projectID).setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of(bucketName, file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, Paths.get(file.getPath()));
        System.out.println("File from: " + file.getPath() + " uploaded to " + bucketName);
    }
    public static void uploadFile(File file) throws IOException {
        uploadFile(projectID, bucketName, file);
    }
}
