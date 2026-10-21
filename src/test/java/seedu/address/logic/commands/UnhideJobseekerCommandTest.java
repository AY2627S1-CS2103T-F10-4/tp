package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnhideJobseekerCommand}.
 */
public class UnhideJobseekerCommandTest {
    @TempDir
    public Path testFolder;

    private final Model model = new ModelManager();
    private final Jobseeker hiddenPerson = new JobseekerBuilder(ALICE).withRemark("Keep this remark")
            .withTags("priority").withHidden(true).build();

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnhideJobseekerCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnhideJobseekerCommand(INDEX_FIRST_PERSON).execute(null));
    }

    /**
     * Restoring from the full hidden list preserves all details and removes the person from that view.
     */
    @Test
    public void execute_hiddenList_success() {
        model.addJobseeker(hiddenPerson);
        new ListHiddenJobseekerCommand().execute(model);
        Jobseeker restoredPerson = new JobseekerBuilder(hiddenPerson).withHidden(false).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(hiddenPerson, restoredPerson);

        assertCommandSuccess(new UnhideJobseekerCommand(INDEX_FIRST_PERSON), model,
                String.format(UnhideJobseekerCommand.MESSAGE_UNHIDE_JOBSEEKER_SUCCESS, Messages.format(restoredPerson)),
                expectedModel);
        assertTrue(model.getFilteredJobseekerList().isEmpty());
        assertEquals(List.of(restoredPerson), model.getAddressBook().getJobseekerList());
    }

    @Test
    public void execute_hiddenPerson_visibleInListAndSearch() throws Exception {
        model.addJobseeker(hiddenPerson);
        new ListHiddenJobseekerCommand().execute(model);
        new UnhideJobseekerCommand(INDEX_FIRST_PERSON).execute(model);
        Jobseeker restoredPerson = new JobseekerBuilder(hiddenPerson).withHidden(false).build();

        new ListJobseekerCommand().execute(model);
        assertEquals(List.of(restoredPerson), model.getFilteredJobseekerList());
        new FindJobseekerCommand(new PersonContainsKeywordsPredicate(List.of(hiddenPerson.getEmail().value)))
                .execute(model);
        assertEquals(List.of(restoredPerson), model.getFilteredJobseekerList());
    }

    @Test
    public void execute_saveAndReload_staysUnhidden() throws Exception {
        model.addJobseeker(hiddenPerson);
        new ListHiddenJobseekerCommand().execute(model);
        new UnhideJobseekerCommand(INDEX_FIRST_PERSON).execute(model);
        Jobseeker restoredPerson = new JobseekerBuilder(hiddenPerson).withHidden(false).build();

        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve("addressbook.json"));
        storage.saveAddressBook(model.getAddressBook());
        Model reloadedModel = new ModelManager(storage.readAddressBook().orElseThrow(), new UserPrefs());
        assertEquals(List.of(restoredPerson), reloadedModel.getFilteredJobseekerList());
    }

    /**
     * Restoring the only search result uses its displayed index and keeps other hidden people filtered out.
     */
    @Test
    public void execute_hiddenSearch_success() {
        Jobseeker otherPerson = new JobseekerBuilder().withName("Other Person").withPhone("88887777")
                .withEmail("other@example.com").withHidden(true).build();
        model.addJobseeker(otherPerson);
        model.addJobseeker(hiddenPerson);
        new FindHiddenJobseekerCommand(new PersonContainsKeywordsPredicate(List.of(hiddenPerson.getEmail().value)))
                .execute(model);

        Jobseeker restoredPerson = new JobseekerBuilder(hiddenPerson).withHidden(false).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(hiddenPerson, restoredPerson);
        String expectedMessage = String.format(UnhideJobseekerCommand.MESSAGE_UNHIDE_JOBSEEKER_SUCCESS,
                Messages.format(restoredPerson));

        assertCommandSuccess(new UnhideJobseekerCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredJobseekerList().isEmpty());
        assertEquals(List.of(otherPerson, restoredPerson),
                model.getAddressBook().getJobseekerList());
        new ListHiddenJobseekerCommand().execute(model);
        assertEquals(List.of(otherPerson), model.getFilteredJobseekerList());
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        assertCommandFailure(new UnhideJobseekerCommand(INDEX_FIRST_PERSON), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidListIndex_throwsCommandException() {
        model.addJobseeker(hiddenPerson);
        new ListHiddenJobseekerCommand().execute(model);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobseekerList().size() + 1);

        assertCommandFailure(new UnhideJobseekerCommand(outOfBoundIndex), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    /**
     * An index within the address book can still be outside the displayed search results.
     */
    @Test
    public void execute_invalidSearchIndex_throwsCommandException() {
        model.addJobseeker(hiddenPerson);
        model.addJobseeker(new JobseekerBuilder().withPhone("88887777").withEmail("other@example.com")
                .withHidden(true).build());
        new FindHiddenJobseekerCommand(new PersonContainsKeywordsPredicate(List.of(hiddenPerson.getEmail().value)))
                .execute(model);
        assertTrue(INDEX_SECOND_PERSON.getZeroBased() < model.getAddressBook().getJobseekerList().size());

        assertCommandFailure(new UnhideJobseekerCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyUnhidden_noChange() {
        model.addJobseeker(ALICE);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new UnhideJobseekerCommand(INDEX_FIRST_PERSON), model,
                UnhideJobseekerCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredJobseekerList());
    }

    @Test
    public void equals() {
        UnhideJobseekerCommand command = new UnhideJobseekerCommand(INDEX_FIRST_PERSON);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new UnhideJobseekerCommand(INDEX_FIRST_PERSON)));
        assertFalse(command.equals(new UnhideJobseekerCommand(INDEX_SECOND_PERSON)));
        assertFalse(command.equals(new HideJobseekerCommand(INDEX_FIRST_PERSON)));
        assertFalse(command.equals(null));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = INDEX_FIRST_PERSON;
        UnhideJobseekerCommand command = new UnhideJobseekerCommand(targetIndex);
        String expected = UnhideJobseekerCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, command.toString());
    }
}
