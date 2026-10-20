package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code ListHiddenJobseekerCommand}.
 */
public class ListHiddenJobseekerCommandTest {

    /**
     * Starts from the default view, which leaves hidden jobseekers out, and checks that the command
     * switches to showing exactly the hidden ones, without changing the address book.
     */
    @Test
    public void execute_someJobseekersHidden_listsOnlyHiddenJobseekers() {
        Jobseeker hiddenBenson = new JobseekerBuilder(BENSON).withHidden(true).build();
        Jobseeker hiddenCarl = new JobseekerBuilder(CARL).withHidden(true).build();
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(hiddenBenson);
        model.addJobseeker(hiddenCarl);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new ListHiddenJobseekerCommand(), model, ListHiddenJobseekerCommand.MESSAGE_SUCCESS,
                expectedModel);
        assertEquals(List.of(hiddenBenson, hiddenCarl), model.getFilteredJobseekerList());
    }

    @Test
    public void execute_noJobseekersHidden_listsNothing() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new ListHiddenJobseekerCommand(), model, ListHiddenJobseekerCommand.MESSAGE_SUCCESS,
                expectedModel);
        assertTrue(model.getFilteredJobseekerList().isEmpty());
    }

    /**
     * Follows what a recruiter does: hide a jobseeker with {@code hidej}, then find them again with
     * this command.
     */
    @Test
    public void execute_afterHidej_showsHiddenJobseeker() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Jobseeker jobseekerToHide = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        new HideJobseekerCommand(INDEX_FIRST_PERSON).execute(model);

        new ListHiddenJobseekerCommand().execute(model);

        assertEquals(List.of(new JobseekerBuilder(jobseekerToHide).withHidden(true).build()),
                model.getFilteredJobseekerList());
    }

    /**
     * Only the jobseeker list switches to hidden jobseekers; the client list must keep showing what it
     * showed before. The test starts from a search result rather than the default view, so that it
     * fails on any change to the client list, including a reset to the default view.
     */
    @Test
    public void execute_clientListFiltered_clientListUnchanged() {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);
        showClientAtIndex(model, INDEX_SECOND_PERSON);

        new ListHiddenJobseekerCommand().execute(model);

        assertEquals(List.of(BRENDA), model.getFilteredClientList());
    }
}
