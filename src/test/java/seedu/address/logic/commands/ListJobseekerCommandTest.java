package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListJobseekerCommand.
 */
public class ListJobseekerCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_jobseekerListIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListJobseekerCommand(), model, ListJobseekerCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_jobseekerListIsFiltered_showsEverything() {
        showJobseekerAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListJobseekerCommand(), model, ListJobseekerCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Jobseekers that are hidden must be left out of the list, even when the previous view showed them,
     * but must stay in the address book.
     */
    @Test
    public void execute_hiddenJobseeker_notListed() {
        Jobseeker hiddenJobseeker = new JobseekerBuilder(BENSON).withHidden(true).build();
        Model hiddenModel = new ModelManager();
        hiddenModel.addJobseeker(ALICE);
        hiddenModel.addJobseeker(hiddenJobseeker);
        hiddenModel.updateFilteredJobseekerList(jobseeker -> true);

        new ListJobseekerCommand().execute(hiddenModel);

        assertEquals(List.of(ALICE), hiddenModel.getFilteredJobseekerList());
        assertEquals(List.of(ALICE, hiddenJobseeker), hiddenModel.getAddressBook().getJobseekerList());
    }
}
