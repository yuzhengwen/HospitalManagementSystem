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
    private final static String FILEPATH = "src/Data/";

    /**
     * Write fixed content to the given file.
     */
    public void saveData(String fileName, List<String> data) {

        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (String datum : data) {
                out.println(datum);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the contents of the given file.
     */
    public List<String> readData(String fileName) {
        List<String> data = new ArrayList<String>();
        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
