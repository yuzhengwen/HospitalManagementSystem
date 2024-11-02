package DataHandling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for reading and writing data to and from local files.
 */
public class LocalFileHandler implements ISaveService {
    private final static String SEPARATOR = "|";
    private final static String FOLDERPATH = "./src/CSV/";

    /**
     * Write fixed content to the given file.
     */
    public void saveData(String fileName, List<String> data) {
        // check if file exists, if not create it
        File file = new File(FOLDERPATH + fileName);
        try {
            if (file.createNewFile())
                System.out.println("File created: " + file.getName());
            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
                for (String datum : data) {
                    out.println(datum);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the contents of the given file.
     */
    public List<String> readData(String fileName) {
        // check if file exists
        File file = new File(FOLDERPATH + fileName);
        if (!file.exists()) {
            return null;
        }

        List<String> data = new ArrayList<String>();
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
