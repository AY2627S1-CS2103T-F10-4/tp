package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddJobseekerCommand}.
 */
public class AddJobseekerCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newJobseeker_success() {
        Jobseeker validJobseeker = new JobseekerBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addJobseeker(validJobseeker);

        assertCommandSuccess(new AddJobseekerCommand(validJobseeker), model,
                String.format(AddJobseekerCommand.MESSAGE_SUCCESS, Messages.format(validJobseeker)),
                expectedModel);
    }

    @Test
    public void execute_duplicateJobseeker_throwsCommandException() {
        Jobseeker jobseekerInList = model.getAddressBook().getJobseekerList().get(0);
        assertCommandFailure(new AddJobseekerCommand(jobseekerInList), model,
                AddJobseekerCommand.MESSAGE_DUPLICATE_JOBSEEKER);
    }

}
