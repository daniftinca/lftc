package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Service class that has methods to read and write files.
 */
public class FileHandlerService {


    /**
     * Outputs a list of Strings, each representing a line.
     *
     * @param filePath - path to the file
     * @return List of Strings (lines)
     * @throws IOException
     */
    public List<String> readFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

}
