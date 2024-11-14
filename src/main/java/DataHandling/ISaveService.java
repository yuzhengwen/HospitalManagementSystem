package DataHandling;

import java.util.List;

/*
 * This interface is responsible for reading and writing data to and from various sources.
 * Can be to local files, databases, cloud storage, etc.
 */
public interface ISaveService {
    void saveData(String fileName, List<String> data);
    List<String> readData(String fileName);
}
