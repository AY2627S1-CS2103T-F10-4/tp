package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.EditJobseekerDescriptorBuilder;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditJobseekerCommand.
 */
public class EditJobseekerCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Jobseeker editedJobseeker = new JobseekerBuilder().build();
        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptorBuilder(editedJobseeker).build();
        EditJobseekerCommand editCommand = new EditJobseekerCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditJobseekerCommand.MESSAGE_EDIT_JOBSEEKER_SUCCESS,
                Messages.format(editedJobseeker));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setJobseeker(model.getFilteredJobseekerList().get(0), editedJobseeker);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastJobseeker = Index.fromOneBased(model.getFilteredJobseekerList().size());
        Jobseeker lastJobseeker = model.getFilteredJobseekerList().get(indexLastJobseeker.getZeroBased());

        JobseekerBuilder jobseekerInList = new JobseekerBuilder(lastJobseeker);
        Jobseeker editedJobseeker = jobseekerInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditJobseekerCommand editCommand = new EditJobseekerCommand(indexLastJobseeker, descriptor);

        String expectedMessage = String.format(EditJobseekerCommand.MESSAGE_EDIT_JOBSEEKER_SUCCESS,
                Messages.format(editedJobseeker));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setJobseeker(lastJobseeker, editedJobseeker);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditJobseekerCommand editCommand = new EditJobseekerCommand(INDEX_FIRST_PERSON, new EditJobseekerDescriptor());
        Jobseeker editedJobseeker = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditJobseekerCommand.MESSAGE_EDIT_JOBSEEKER_SUCCESS,
                Messages.format(editedJobseeker));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setJobseeker(editedJobseeker, editedJobseeker);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Jobseeker jobseekerInFilteredList = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        Jobseeker editedJobseeker = new JobseekerBuilder(jobseekerInFilteredList).withName(VALID_NAME_BOB).build();
        EditJobseekerCommand editCommand = new EditJobseekerCommand(INDEX_FIRST_PERSON,
                new EditJobseekerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditJobseekerCommand.MESSAGE_EDIT_JOBSEEKER_SUCCESS,
                Messages.format(editedJobseeker));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setJobseeker(model.getFilteredJobseekerList().get(0), editedJobseeker);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateJobseekerUnfilteredList_failure() {
        Jobseeker firstJobseeker = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptorBuilder(firstJobseeker).build();
        EditJobseekerCommand editCommand = new EditJobseekerCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditJobseekerCommand.MESSAGE_DUPLICATE_JOBSEEKER);
    }

    @Test
    public void execute_duplicateJobseekerFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit jobseeker in filtered list into a duplicate in address book
        Jobseeker jobseekerInList = model.getAddressBook().getJobseekerList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditJobseekerCommand editCommand = new EditJobseekerCommand(INDEX_FIRST_PERSON,
                new EditJobseekerDescriptorBuilder(jobseekerInList).build());

        assertCommandFailure(editCommand, model, EditJobseekerCommand.MESSAGE_DUPLICATE_JOBSEEKER);
    }

    @Test
    public void execute_invalidJobseekerIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobseekerList().size() + 1);
        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditJobseekerCommand editCommand = new EditJobseekerCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered jobseeker list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidJobseekerIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getJobseekerList().size());

        EditJobseekerCommand editCommand = new EditJobseekerCommand(outOfBoundIndex,
                new EditJobseekerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditJobseekerCommand standardCommand = new EditJobseekerCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditJobseekerDescriptor copyDescriptor = new EditJobseekerDescriptor(DESC_AMY);
        EditJobseekerCommand commandWithSameValues = new EditJobseekerCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditJobseekerCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditJobseekerCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditJobseekerDescriptor editJobseekerDescriptor = new EditJobseekerDescriptor();
        EditJobseekerCommand editCommand = new EditJobseekerCommand(index, editJobseekerDescriptor);
        String expected = EditJobseekerCommand.class.getCanonicalName()
                + "{index=" + index + ", editJobseekerDescriptor=" + editJobseekerDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
