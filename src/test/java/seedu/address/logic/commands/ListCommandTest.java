package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Tests resetting both lists through ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listsAreNotFiltered_showsSameLists() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(expectedModel.getFilteredJobseekerList(), model.getFilteredJobseekerList());
        assertEquals(expectedModel.getFilteredClientList(), model.getFilteredClientList());
    }

    @Test
    public void execute_bothListsAreFiltered_showsAllUnhiddenRecords() {
        showJobseekerAtIndex(model, INDEX_FIRST_PERSON);
        showClientAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(expectedModel.getFilteredJobseekerList(), model.getFilteredJobseekerList());
        assertEquals(expectedModel.getFilteredClientList(), model.getFilteredClientList());
    }

    @Test
    public void execute_bothListsShowHiddenRecords_restoresUnhiddenViewsWithoutChangingData() {
        Jobseeker hiddenJobseeker = new JobseekerBuilder(BENSON).withHidden(true).build();
        Client hiddenClient = new ClientBuilder(BRENDA).withHidden(true).build();
        Model hiddenModel = new ModelManager();
        hiddenModel.addJobseeker(ALICE);
        hiddenModel.addJobseeker(hiddenJobseeker);
        hiddenModel.addClient(ADRIAN);
        hiddenModel.addClient(hiddenClient);
        new ListHiddenJobseekerCommand().execute(hiddenModel);
        new ListHiddenClientCommand().execute(hiddenModel);
        assertEquals(List.of(hiddenJobseeker), hiddenModel.getFilteredJobseekerList());
        assertEquals(List.of(hiddenClient), hiddenModel.getFilteredClientList());

        CommandResult result = new ListCommand().execute(hiddenModel);

        assertEquals(new CommandResult(ListCommand.MESSAGE_SUCCESS), result);
        assertEquals(List.of(ALICE), hiddenModel.getFilteredJobseekerList());
        assertEquals(List.of(ADRIAN), hiddenModel.getFilteredClientList());
        assertEquals(List.of(ALICE, hiddenJobseeker), hiddenModel.getAddressBook().getJobseekerList());
        assertEquals(List.of(ADRIAN, hiddenClient), hiddenModel.getAddressBook().getClientList());
    }

    @Test
    public void execute_emptyAddressBook_success() {
        assertCommandSuccess(new ListCommand(), new ModelManager(), ListCommand.MESSAGE_SUCCESS, new ModelManager());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListCommand().execute(null));
    }
}
