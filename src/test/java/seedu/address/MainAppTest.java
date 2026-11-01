package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

/**
 * Tests the startup fallbacks in {@code MainApp}: what the app loads when the preferences file or
 * data file is missing, invalid or cannot be saved. Every file lives in a temporary folder, so these
 * tests never touch the real save files.
 */
public class MainAppTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data");
    private static final String NOT_JSON = "{ this is not valid JSON";

    @TempDir
    public Path testFolder;

    private final MainApp mainApp = new MainApp();

    //=========== initPrefs ==================================================================================

    @Test
    public void initPrefs_missingFile_usesDefaultsAndCreatesFile() throws Exception {
        // EP: no prefs file
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(getPrefsFile());

        assertEquals(new UserPrefs(), mainApp.initPrefs(storage));
        assertEquals(Optional.of(new UserPrefs()), storage.readUserPrefs());
    }

    @Test
    public void initPrefs_invalidContent_usesDefaultsAndRepairsFile() throws Exception {
        // EP: content that is not JSON
        assertInvalidPrefsReplacedByDefaults(NOT_JSON);
        assertInvalidPrefsReplacedByDefaults(""); // Boundary value

        // EP: JSON that does not match the structure of the prefs
        assertInvalidPrefsReplacedByDefaults("[1, 2]");
        assertInvalidPrefsReplacedByDefaults("{\"guiSettings\": \"not an object\"}");
    }

    @Disabled("Known bug: JsonUtil#readJsonFile throws NullPointerException when a file contains null")
    @Test
    public void initPrefs_jsonNull_usesDefaultsAndRepairsFile() throws Exception {
        // EP: JSON that does not match the structure of the prefs
        assertInvalidPrefsReplacedByDefaults("null"); // Boundary value
    }

    @Disabled("Known bug: initPrefs accepts null guiSettings, which ModelManager then rejects")
    @Test
    public void initPrefs_nullGuiSettings_usesDefaultsAndRepairsFile() throws Exception {
        // EP: JSON that does not match the structure of the prefs
        assertInvalidPrefsReplacedByDefaults("{\"guiSettings\": null}"); // Boundary value
    }

    /**
     * Checks that prefs read from a valid file are used, rather than being replaced by the defaults.
     * An empty JSON object still counts as valid, so any value missing from the file takes its default.
     */
    @Test
    public void initPrefs_validContent_usesFileValues() throws Exception {
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(getPrefsFile());

        // EP: valid prefs with every value present
        storage.saveUserPrefs(getCustomPrefs());
        assertEquals(getCustomPrefs(), mainApp.initPrefs(storage));

        // EP: valid prefs with values missing
        Files.writeString(getPrefsFile(), "{}");
        assertEquals(new UserPrefs(), mainApp.initPrefs(storage)); // Boundary value
    }

    /**
     * Guards the re-save at the end of {@code initPrefs}, which removes fields the app no longer uses.
     */
    @Test
    public void initPrefs_extraValuesInFile_ignoresAndRemovesThem() throws Exception {
        // EP: valid prefs with unknown values
        Files.copy(TEST_DATA_FOLDER.resolve("JsonUserPrefsStorageTest").resolve("ExtraValuesUserPref.json"),
                getPrefsFile());

        assertEquals(getCustomPrefs(), mainApp.initPrefs(new JsonUserPrefsStorage(getPrefsFile())));
        assertFalse(Files.readString(getPrefsFile()).contains("addressBookFilePath"));
    }

    /**
     * Failing to save must not discard the prefs that were already loaded.
     */
    @Test
    public void initPrefs_saveFails_stillReturnsPrefs() throws Exception {
        JsonUserPrefsStorage storage = new SaveFailingUserPrefsStorage(getPrefsFile());

        // EP: no prefs file
        assertEquals(new UserPrefs(), mainApp.initPrefs(storage));

        // EP: content that is not JSON
        Files.writeString(getPrefsFile(), NOT_JSON);
        assertEquals(new UserPrefs(), mainApp.initPrefs(storage));

        // EP: valid prefs with every value present
        new JsonUserPrefsStorage(getPrefsFile()).saveUserPrefs(getCustomPrefs());
        assertEquals(getCustomPrefs(), mainApp.initPrefs(storage));
    }

    //=========== initModelManager and saveInitialAddressBook ================================================

    @Test
    public void initModelManager_missingFile_loadsAndSavesSampleData() throws Exception {
        // EP: no data file
        Storage storage = createStorage();
        AddressBook sampleData = new AddressBook(SampleDataUtil.getSampleAddressBook());

        assertEquals(sampleData, initModelManager(storage).getAddressBook());
        assertEquals(sampleData, new AddressBook(storage.readAddressBook().orElseThrow()));
    }

    /**
     * Failing to save the sample data on the first run must not stop the app from starting.
     */
    @Test
    public void initModelManager_missingFileAndSaveFails_stillLoadsSampleData() throws Exception {
        // EP: no data file
        Storage storage = new SaveFailingStorageManager(getDataFile(), getPrefsFile());

        assertEquals(new AddressBook(SampleDataUtil.getSampleAddressBook()),
                initModelManager(storage).getAddressBook());
        assertFalse(Files.exists(getDataFile()));
    }

    /**
     * An empty address book must stay empty: a recruiter who has deleted everyone should not get the
     * sample data back, because that only happens when the file is missing.
     */
    @Test
    public void initModelManager_validFile_loadsFileData() throws Exception {
        // EP: valid data file
        assertSavedDataLoaded(getTypicalAddressBook());
        assertSavedDataLoaded(new AddressBook()); // Boundary value

        AddressBook onePerson = new AddressBook();
        onePerson.addJobseeker(ALICE);
        assertSavedDataLoaded(onePerson); // Boundary value

        // EP: valid data file with values missing
        Files.writeString(getDataFile(), "{}");
        Model model = initModelManager(createStorage());
        assertEquals(new AddressBook(), model.getAddressBook()); // Boundary value
    }

    /**
     * The invalid file must be left as it is, so that a recruiter can still recover their data from it.
     */
    @Test
    public void initModelManager_invalidFile_startsEmptyAndKeepsFile() throws Exception {
        // EP: content that is not JSON
        assertInvalidDataIgnoredAndKept(NOT_JSON);
        assertInvalidDataIgnoredAndKept(""); // Boundary value

        // EP: JSON that does not match the structure of the data
        assertInvalidDataIgnoredAndKept("[1, 2]");

        // EP: JSON with data that breaks a constraint
        assertInvalidDataIgnoredAndKept(
                readTestData("JsonAddressBookStorageTest", "invalidPersonAddressBook.json"));
        assertInvalidDataIgnoredAndKept(
                readTestData("JsonSerializableAddressBookTest", "duplicateJobseekerAddressBook.json"));
        assertInvalidDataIgnoredAndKept(
                readTestData("JsonAddressBookStorageTest", "invalidAndValidPersonAddressBook.json"));
    }

    @Disabled("Known bug: JsonUtil#readJsonFile throws NullPointerException when a file contains null")
    @Test
    public void initModelManager_jsonNull_startsEmptyAndKeepsFile() throws Exception {
        // EP: JSON that does not match the structure of the data
        assertInvalidDataIgnoredAndKept("null"); // Boundary value
    }

    @Test
    public void initModelManager_anyDataFile_usesGivenPrefs() throws Exception {
        // EP: no data file
        assertEquals(getCustomPrefs(), initModelManager(createStorage()).getUserPrefs());

        // EP: content that is not JSON
        Files.writeString(getDataFile(), NOT_JSON);
        assertEquals(getCustomPrefs(), initModelManager(createStorage()).getUserPrefs());
    }

    //=========== Helpers ====================================================================================

    private Path getPrefsFile() {
        return testFolder.resolve("prefs.json");
    }

    private Path getDataFile() {
        return testFolder.resolve("addressbook.json");
    }

    private Storage createStorage() {
        return new StorageManager(new JsonAddressBookStorage(getDataFile()),
                new JsonUserPrefsStorage(getPrefsFile()));
    }

    private UserPrefs getCustomPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(1000, 500, 300, 100));
        return userPrefs;
    }

    private String readTestData(String folderName, String fileName) throws IOException {
        return Files.readString(TEST_DATA_FOLDER.resolve(folderName).resolve(fileName));
    }

    /**
     * Calls the private {@code MainApp#initModelManager} through reflection, so that testing it does not
     * need a change to the production code. If the method is renamed, this fails with
     * {@code NoSuchMethodException}.
     */
    private Model initModelManager(Storage storage) throws ReflectiveOperationException {
        Method method = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, ReadOnlyUserPrefs.class);
        method.setAccessible(true);
        return (Model) method.invoke(mainApp, storage, getCustomPrefs());
    }

    private void assertInvalidPrefsReplacedByDefaults(String fileContent) throws Exception {
        Files.writeString(getPrefsFile(), fileContent);
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(getPrefsFile());

        assertEquals(new UserPrefs(), mainApp.initPrefs(storage));
        assertEquals(Optional.of(new UserPrefs()), storage.readUserPrefs());
    }

    private void assertSavedDataLoaded(AddressBook addressBook) throws Exception {
        Storage storage = createStorage();
        storage.saveAddressBook(addressBook);

        assertEquals(addressBook, initModelManager(storage).getAddressBook());
    }

    private void assertInvalidDataIgnoredAndKept(String fileContent) throws Exception {
        Files.writeString(getDataFile(), fileContent);

        assertEquals(new AddressBook(), initModelManager(createStorage()).getAddressBook());
        assertEquals(fileContent, Files.readString(getDataFile()));
    }

    /**
     * A {@code JsonUserPrefsStorage} that reads normally but always fails to save.
     */
    private static class SaveFailingUserPrefsStorage extends JsonUserPrefsStorage {
        SaveFailingUserPrefsStorage(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            throw new IOException("Simulated failure to save prefs");
        }
    }

    /**
     * A {@code StorageManager} that reads normally but always fails to save the address book.
     */
    private static class SaveFailingStorageManager extends StorageManager {
        SaveFailingStorageManager(Path dataFile, Path prefsFile) {
            super(new JsonAddressBookStorage(dataFile), new JsonUserPrefsStorage(prefsFile));
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            throw new IOException("Simulated failure to save address book");
        }
    }
}
