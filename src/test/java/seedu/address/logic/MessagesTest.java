package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class MessagesTest {
    @Test
    public void format_populatedOptionalFields_includesValues() {
        assertTrue(Messages.format(new JobseekerBuilder().withHighestQualification("Diploma in IT").build())
                .contains("Highest qualification: Diploma in IT"));
        assertTrue(Messages.format(new ClientBuilder().withAvailableJobs("QA Tester", "Engineer").build())
                .contains("Available jobs: Engineer, QA Tester"));
    }

    @Test
    public void format_emptyOptionalFields_omitsLabels() {
        assertFalse(Messages.format(new JobseekerBuilder().build()).contains("Highest qualification:"));
        assertFalse(Messages.format(new ClientBuilder().build()).contains("Available jobs:"));
    }
}
