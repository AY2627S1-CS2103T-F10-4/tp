package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.JobseekerBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(List.of(), addressBook.getJobseekerList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two jobseekers with the same identity fields
        Jobseeker editedAlice = new JobseekerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Jobseeker> newJobseekers = List.of(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newJobseekers);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasJobseeker_jobseekerWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addJobseeker(ALICE);
        Jobseeker editedAlice = new JobseekerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasJobseeker(editedAlice));
    }

    @Test
    public void getJobseekerList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getJobseekerList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{jobseekers=" + addressBook.getJobseekerList()
                + ", clients=" + addressBook.getClientList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose jobseeker list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Jobseeker> jobseekers = FXCollections.observableArrayList();

        AddressBookStub(List<Jobseeker> jobseekers) {
            this.jobseekers.addAll(jobseekers);
        }

        @Override
        public ObservableList<Jobseeker> getJobseekerList() {
            return jobseekers;
        }

        @Override
        public ObservableList<Client> getClientList() {
            return FXCollections.observableArrayList();
        }
    }

}
