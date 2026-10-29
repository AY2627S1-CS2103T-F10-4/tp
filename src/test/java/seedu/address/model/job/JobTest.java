package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class JobTest {

    @Test
    public void constructor_invalidName_rejected() {
        assertThrows(NullPointerException.class, () -> new Job(null));
        assertThrows(IllegalArgumentException.class, () -> new Job(""));
        assertThrows(IllegalArgumentException.class, () -> new Job(" \t "));
    }

    @Test
    public void constructor_jobTitle_allowsSpacesAndPunctuation() {
        Job job = new Job("C++ Developer (Part-time)");
        assertEquals("C++ Developer (Part-time)", job.jobName);
        assertEquals("[C++ Developer (Part-time)]", job.toString());
    }

    @Test
    public void equals_comparesName() {
        Job job = new Job("Software Engineer");
        Job copy = new Job("Software Engineer");
        assertEquals(job, copy);
        assertEquals(job.hashCode(), copy.hashCode());
        assertNotEquals(job, new Job("Data Analyst"));
        assertNotEquals(job, null);
        assertNotEquals(job, "Software Engineer");
    }
}
