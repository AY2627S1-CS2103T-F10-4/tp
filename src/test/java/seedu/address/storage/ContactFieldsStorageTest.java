package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.job.Job;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class ContactFieldsStorageTest {
    private static final String LEGACY_CONTACT = "{\"name\":\"Alice\",\"phone\":\"91234567\","
            + "\"email\":\"alice@example.com\",\"address\":\"Clementi\",\"tags\":[]}";

    @TempDir
    public Path testFolder;

    @Test
    public void saveAndRead_optionalFields_preservedAcrossReloads() throws Exception {
        AddressBook original = new AddressBook();
        original.addJobseeker(new JobseekerBuilder().withHighestQualification("Diploma in IT")
                .withRemark("Available now").withTags("friend").withHidden(true).build());
        original.addClient(new ClientBuilder().withAvailableJobs("Software Engineer", "QA Tester")
                .withRemark("Hiring").withTags("employer").withHidden(true).build());
        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve("contacts.json"));
        storage.saveAddressBook(original);
        assertEquals(original, new AddressBook(storage.readAddressBook().orElseThrow()));

        AddressBook cleared = new AddressBook();
        cleared.addJobseeker(new JobseekerBuilder(original.getJobseekerList().get(0))
                .withHighestQualification("").build());
        cleared.addClient(new ClientBuilder(original.getClientList().get(0)).withAvailableJobs().build());
        storage.saveAddressBook(cleared);
        assertEquals(cleared, new AddressBook(storage.readAddressBook().orElseThrow()));
    }

    @Test
    public void read_missingOptionalFields_defaultsToEmpty() throws Exception {
        assertEquals("", JsonUtil.fromJsonString(LEGACY_CONTACT, JsonAdaptedJobseeker.class)
                .toModelType().getHighestQualification().value);
        assertTrue(JsonUtil.fromJsonString(LEGACY_CONTACT, JsonAdaptedClient.class)
                .toModelType().getAvailableJobs().isEmpty());
    }

    @Test
    public void read_nullOptionalFields_defaultsToEmpty() throws Exception {
        String json = LEGACY_CONTACT.replace("}", ",\"highestQualification\":null,\"availableJobs\":null}");
        assertEquals("", JsonUtil.fromJsonString(json, JsonAdaptedJobseeker.class)
                .toModelType().getHighestQualification().value);
        assertTrue(JsonUtil.fromJsonString(json, JsonAdaptedClient.class)
                .toModelType().getAvailableJobs().isEmpty());
    }

    @Test
    public void read_blankOrNullJob_throwsIllegalValueException() throws Exception {
        for (String jobs : List.of("[\"\"]", "[\"   \"]", "[null]", "[\"Engineer\",null]")) {
            String json = LEGACY_CONTACT.replace("}", ",\"availableJobs\":" + jobs + "}");
            JsonAdaptedClient client = JsonUtil.fromJsonString(json, JsonAdaptedClient.class);
            assertThrows(IllegalValueException.class, Job.MESSAGE_CONSTRAINTS, client::toModelType);
        }
    }
}
