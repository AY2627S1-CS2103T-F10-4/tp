package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

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
}
