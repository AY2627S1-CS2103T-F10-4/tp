package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * Contains tests for {@link SampleDataUtil}.
 */
public class SampleDataUtilTest {

    @Test
    public void getSampleAddressBook_containsSeparateJobseekersAndClients() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertEquals(4, sampleAddressBook.getJobseekerList().size());
        assertEquals(2, sampleAddressBook.getClientList().size());
    }

    /**
     * The sample data is what a first-time user sees, so none of it may start hidden.
     */
    @Test
    public void getSampleAddressBook_everyoneUnhidden() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertTrue(sampleAddressBook.getJobseekerList().stream().noneMatch(Person::isHidden));
        assertTrue(sampleAddressBook.getClientList().stream().noneMatch(Person::isHidden));
    }
}
