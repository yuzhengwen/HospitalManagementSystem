package DataHandling;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static String addQuotes(String str) {
        return '"' + str + '"';
    }

    public static String removeQuotes(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static String[] parseLine(String csvLine) {
        List<String> fields = new ArrayList<>();
        boolean insideQuote = false;
        StringBuilder cell = new StringBuilder();

        for (int i = 0; i < csvLine.length(); i++) {
            char current = csvLine.charAt(i);

            if (insideQuote) {
                if (current == '"') {
                    // Handle escaped quotes
                    if (i + 1 < csvLine.length() && csvLine.charAt(i + 1) == '"') {
                        cell.append('"');
                        i++; // Skip the second quote
                    } else {
                        insideQuote = false; // End of quoted field
                    }
                } else {
                    cell.append(current);
                }
            } else {
                if (current == '"') {
                    insideQuote = true; // Start of quoted field
                } else if (current == ',') {
                    fields.add(cell.toString());
                    cell.setLength(0); // Reset cell
                } else {
                    cell.append(current);
                }
            }
        }

        // Add the last field
        fields.add(cell.toString());

        return fields.toArray(new String[0]);
    }
}
