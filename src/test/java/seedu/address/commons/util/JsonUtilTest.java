package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.testutil.SerializableTestClass;
import seedu.address.testutil.TestUtil;

/**
 * Tests JSON Read and Write
 */
public class JsonUtilTest {

    private static final Path SERIALIZATION_FILE = TestUtil.getFilePathInSandboxFolder("serialize.json");

    @TempDir
    public Path testFolder;

    @Test
    public void readJsonFile_missingFile_returnsEmpty() throws Exception {
        // EP: no file
        Path missingFile = testFolder.resolve("missing.json");
        assertEquals(Optional.empty(), JsonUtil.readJsonFile(missingFile, SerializableTestClass.class));
    }

    @Test
    public void readJsonFile_validContent_returnsObject() throws Exception {
        // EP: JSON object with every value present
        assertEquals(SerializableTestClass.getNameTestValue(),
                readJsonContent(SerializableTestClass.JSON_STRING_REPRESENTATION).orElseThrow().getName());

        // EP: JSON object with values missing
        assertNull(readJsonContent("{}").orElseThrow().getName()); // Boundary value
    }

    /**
     * A file containing only the JSON value null parses without error, so it needs its own check. It must
     * fail with {@code DataLoadingException}, which is what callers catch to fall back on default data.
     */
    @Test
    public void readJsonFile_invalidContent_throwsDataLoadingException() {
        // EP: content that is not JSON
        assertReadFails("{ this is not valid JSON");
        assertReadFails(""); // Boundary value

        // EP: JSON that does not match the structure of the class
        assertReadFails("[1, 2]");

        // EP: JSON that holds no object
        assertReadFails("null"); // Boundary value
        assertReadFails("  null\n");
    }

    private Optional<SerializableTestClass> readJsonContent(String fileContent) throws Exception {
        Path jsonFile = testFolder.resolve("test.json");
        Files.writeString(jsonFile, fileContent);
        return JsonUtil.readJsonFile(jsonFile, SerializableTestClass.class);
    }

    private void assertReadFails(String fileContent) {
        assertThrows(DataLoadingException.class, () -> readJsonContent(fileContent));
    }

    @Test
    public void serializeObjectToJsonFile_noExceptionThrown() throws IOException {
        SerializableTestClass serializableTestClass = new SerializableTestClass();
        serializableTestClass.setTestValues();

        JsonUtil.serializeObjectToJsonFile(SERIALIZATION_FILE, serializableTestClass);

        assertEquals(FileUtil.readFromFile(SERIALIZATION_FILE), SerializableTestClass.JSON_STRING_REPRESENTATION);
    }

    @Test
    public void deserializeObjectFromJsonFile_noExceptionThrown() throws IOException {
        FileUtil.writeToFile(SERIALIZATION_FILE, SerializableTestClass.JSON_STRING_REPRESENTATION);

        SerializableTestClass serializableTestClass = JsonUtil
                .deserializeObjectFromJsonFile(SERIALIZATION_FILE, SerializableTestClass.class);

        assertEquals(serializableTestClass.getName(), SerializableTestClass.getNameTestValue());
        assertEquals(serializableTestClass.getListOfLocalDateTimes(), SerializableTestClass.getListTestValues());
        assertEquals(serializableTestClass.getMapOfIntegerToString(), SerializableTestClass.getHashMapTestValues());
    }

    //TODO: @Test jsonUtil_readJsonStringToObjectInstance_correctObject()

    //TODO: @Test jsonUtil_writeThenReadObjectToJson_correctObject()
}
