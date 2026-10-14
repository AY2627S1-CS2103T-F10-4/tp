package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.JobseekerBuilder;

public class RemarkTest {
    @Test
    public void constructor_null_rejected() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void constructor_empty_allowed() {
        assertEquals("", new Remark("").value);
    }

    @Test
    public void equals_comparesText() {
        Remark remark = new Remark("Available immediately");
        Remark copy = new Remark("Available immediately");
        assertEquals(remark, copy);
        assertEquals(remark.hashCode(), copy.hashCode());
        assertNotEquals(remark, new Remark(""));
        assertNotEquals(remark, null);
        assertNotEquals(remark, "Available immediately");
    }

    @Test
    public void person_remarkAffectsEqualityButNotIdentity() {
        Jobseeker original = new JobseekerBuilder().withRemark("Available immediately").build();
        Jobseeker edited = new JobseekerBuilder(original).withRemark("Available next month").build();
        assertNotEquals(original, edited);
        assertTrue(original.isSamePerson(edited));
    }
}
