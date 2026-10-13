package seedu.address.commons.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes and reads files
 */
public class FileUtil {

    private static final String CHARSET = "UTF-8";

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     *
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(Path file) throws IOException {
        if (Files.exists(file)) {
            return;
        }

        createParentDirsOfFile(file);

        Files.createFile(file);
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    private static void createParentDirsOfFile(Path file) throws IOException {
        Path parentDir = file.getParent();

        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Returns the content of the given file as a string.
     * Assumes the file exists.
     *
     * @param file the text file to read (e.g. a JSON file), encoded in {@code CHARSET} (UTF-8).
     * @return the content of the file.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    /**
     * Writes the given string to a file.
     * Creates the file if it does not exist yet, or overwrites it if it already exists.
     *
     * @param file the text file to write to (e.g. a JSON file), encoded in {@code CHARSET} (UTF-8).
     * @param content the content to write to the file.
     * @throws IOException if an I/O error occurs while writing.
     */
    public static void writeToFile(Path file, String content) throws IOException {
        Files.write(file, content.getBytes(CHARSET));
    }

}
