package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Remark;
import seedu.address.testutil.EditJobseekerDescriptorBuilder;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Tests {@code EditJobseekerDescriptor}, which holds the fields to change in an edit jobseeker command.
 * Covers remark edits and copying, equality, and the string representation.
 */
public class EditJobseekerDescriptorTest {

    @Test
    public void remark_emptyValueCountsAsEditAndCopyPreservesValue() {
        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptor();
        assertTrue(descriptor.getRemark().isEmpty());
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor.setRemark(new Remark(""));
        assertTrue(descriptor.isAnyFieldEdited());
        EditJobseekerDescriptor copy = new EditJobseekerDescriptor(descriptor);
        assertEquals(new Remark(""), copy.getRemark().orElseThrow());
        assertEquals(descriptor, copy);

        descriptor.setRemark(null);
        assertFalse(descriptor.isAnyFieldEdited());
        assertFalse(descriptor.equals(copy));
        assertEquals(new Remark(""), copy.getRemark().orElseThrow());
    }

    @Test
    public void equals() {
        // same values -> returns true
        EditJobseekerDescriptor descriptorWithSameValues = new EditJobseekerDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditJobseekerDescriptor editedAmy = new EditJobseekerDescriptorBuilder(DESC_AMY)
                .withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different remark -> returns false
        editedAmy = new EditJobseekerDescriptorBuilder(DESC_AMY).withRemark("Available immediately").build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditJobseekerDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditJobseekerDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditJobseekerDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditJobseekerDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    /**
     * Sets every field because an empty descriptor prints all its values as null, which would not
     * reveal a field whose value is printed under the wrong label.
     */
    @Test
    public void toStringMethod_allFieldsSet_listsEveryField() {
        Jobseeker jobseeker = new JobseekerBuilder().withTags("priority").withRemark("Open to work")
                .withHighestQualification("Bachelor of Science").build();
        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptor();
        descriptor.setName(jobseeker.getName());
        descriptor.setPhone(jobseeker.getPhone());
        descriptor.setEmail(jobseeker.getEmail());
        descriptor.setAddress(jobseeker.getAddress());
        descriptor.setTags(jobseeker.getTags());
        descriptor.setRemark(jobseeker.getRemark());
        descriptor.setHighestQualification(jobseeker.getHighestQualification());

        String expected = EditJobseekerDescriptor.class.getCanonicalName()
                + "{name=" + jobseeker.getName()
                + ", phone=" + jobseeker.getPhone()
                + ", email=" + jobseeker.getEmail()
                + ", address=" + jobseeker.getAddress()
                + ", tags=" + jobseeker.getTags()
                + ", remark=" + jobseeker.getRemark()
                + ", highestQualification=" + jobseeker.getHighestQualification() + "}";
        assertEquals(expected, descriptor.toString());
    }

    @Test
    public void toStringMethod_noFieldsSet_printsNullValues() {
        EditJobseekerDescriptor editJobseekerDescriptor = new EditJobseekerDescriptor();
        String expected = EditJobseekerDescriptor.class.getCanonicalName() + "{name="
                + editJobseekerDescriptor.getName().orElse(null) + ", phone="
                + editJobseekerDescriptor.getPhone().orElse(null) + ", email="
                + editJobseekerDescriptor.getEmail().orElse(null) + ", address="
                + editJobseekerDescriptor.getAddress().orElse(null) + ", tags="
                + editJobseekerDescriptor.getTags().orElse(null) + ", remark="
                + editJobseekerDescriptor.getRemark().orElse(null) + ", highestQualification="
                + editJobseekerDescriptor.getHighestQualification().orElse(null) + "}";
        assertEquals(expected, editJobseekerDescriptor.toString());
    }
}
