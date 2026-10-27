package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.JobseekerBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new JobseekerBuilder().build(); // JobseekerBuilder builds Jobseeker by default
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same phone number & email, all other attributes different -> returns true
        Person editedAlice = new JobseekerBuilder(ALICE).withName(VALID_NAME_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same phone number only, all other attributes different -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same email only, all other attributes different -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withName(VALID_NAME_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different phone number & email, all other attributes same -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // only hidden status different -> returns true, so a hidden jobseeker still blocks a duplicate
        Person hiddenAlice = new JobseekerBuilder(ALICE).withHidden(true).build();
        assertTrue(ALICE.isSamePerson(hiddenAlice));

        // no need to check for cases and trailing spaces,
        // email and phone numbers have validation built-in
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new JobseekerBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new JobseekerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different hidden status -> returns false
        editedAlice = new JobseekerBuilder(ALICE).withHidden(true).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = ALICE.getClass().getCanonicalName() + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone() + ", email=" + ALICE.getEmail()
                + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags()
                + ", remark=" + ALICE.getRemark() + ", isHidden=" + ALICE.isHidden()
                + ", highestQualification=" + ALICE.getHighestQualification() + "}";
        assertEquals(expected, ALICE.toString());
    }

    /**
     * Checks both {@code Jobseeker} constructors, so that neither can ignore {@code isHidden} and
     * hard-code a status.
     */
    @Test
    public void constructor_withHiddenStatus_keepsHiddenStatus() {
        Jobseeker jobseekerWithoutRemark = new Jobseeker(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), true);
        assertTrue(jobseekerWithoutRemark.isHidden());
        assertEquals(new Remark(""), jobseekerWithoutRemark.getRemark());

        Jobseeker jobseekerWithRemark = new Jobseeker(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), ALICE.getRemark(), true);
        assertTrue(jobseekerWithRemark.isHidden());
    }
}
