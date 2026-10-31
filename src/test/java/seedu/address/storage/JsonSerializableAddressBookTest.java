package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Email;
import seedu.address.testutil.TypicalPersons;

/**
 * Tests loading jobseekers and clients from JSON, including rejection of invalid and duplicate entries.
 */
public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_JOBSEEKER_FILE = TEST_DATA_FOLDER.resolve("invalidJobseekerAddressBook.json");
    private static final Path DUPLICATE_JOBSEEKER_FILE = TEST_DATA_FOLDER.resolve("duplicateJobseekerAddressBook.json");
    private static final Path INVALID_CLIENT_FILE = TEST_DATA_FOLDER.resolve("invalidClientAddressBook.json");
    private static final Path DUPLICATE_CLIENT_FILE = TEST_DATA_FOLDER.resolve("duplicateClientAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidJobseekerFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_JOBSEEKER_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateJobseekers_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_JOBSEEKER_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidClientFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_CLIENT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, Email.MESSAGE_CONSTRAINTS, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateClients_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_CLIENT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    /**
     * A JSON null in either list arrives as a null entry, which must be rejected as invalid data. Otherwise
     * it causes a {@code NullPointerException}, which skips the fallback for invalid files at startup.
     */
    @Test
    public void toModelType_nullEntry_throwsIllegalValueException() throws Exception {
        // EP: null jobseeker entry
        assertNullEntryRejected("{\"jobseekers\": [null]}"); // Boundary value
        String validJobseeker = JsonUtil.toJsonString(new JsonAdaptedJobseeker(TypicalPersons.ALICE));
        assertNullEntryRejected("{\"jobseekers\": [" + validJobseeker + ", null]}");

        // EP: null client entry
        assertNullEntryRejected("{\"clients\": [null]}"); // Boundary value
        String validClient = JsonUtil.toJsonString(new JsonAdaptedClient(TypicalPersons.ADRIAN));
        assertNullEntryRejected("{\"clients\": [" + validClient + ", null]}");
    }

    @Test
    public void toModelType_emptyLists_returnsEmptyAddressBook() throws Exception {
        // EP: lists with no entries
        String json = "{\"jobseekers\": [], \"clients\": []}";
        JsonSerializableAddressBook data = JsonUtil.fromJsonString(json, JsonSerializableAddressBook.class);
        assertEquals(new AddressBook(), data.toModelType()); // Boundary value
    }

    private void assertNullEntryRejected(String json) throws Exception {
        JsonSerializableAddressBook data = JsonUtil.fromJsonString(json, JsonSerializableAddressBook.class);
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_NULL_PERSON,
                data::toModelType);
    }

}
