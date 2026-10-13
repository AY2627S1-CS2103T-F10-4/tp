package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.JobseekerBuilder;

public class UntagJobseekerCommandTest {

    private final Model model = new ModelManager();
    private final Jobseeker jobseeker = new JobseekerBuilder().withTags("java", "teamwork")
            .withRemark("Available immediately").build();

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UntagJobseekerCommand(null, Set.of(new Tag("java"))));
        assertThrows(NullPointerException.class, () -> new UntagJobseekerCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_existingAndMissingTags_preservesDetailsAndOtherTags() {
        model.addJobseeker(jobseeker);
        Jobseeker updated = new JobseekerBuilder(jobseeker).withTags("teamwork").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(jobseeker, updated);

        UntagJobseekerCommand command = new UntagJobseekerCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("java"), new Tag("missing")));
        assertCommandSuccess(command, model,
                String.format(UntagJobseekerCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(Set.of(new Tag("java"), new Tag("teamwork")), jobseeker.getTags());
    }

    @Test
    public void execute_allTags_removesAllTags() {
        model.addJobseeker(jobseeker);
        Jobseeker updated = new JobseekerBuilder(jobseeker).withTags().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(jobseeker, updated);

        assertCommandSuccess(new UntagJobseekerCommand(INDEX_FIRST_PERSON, jobseeker.getTags()), model,
                String.format(UntagJobseekerCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
    }

    @Test
    public void execute_missingTags_noChange() {
        model.addJobseeker(jobseeker);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("Java"))), model,
                UntagJobseekerCommand.MESSAGE_NO_CHANGE, expectedModel);
    }

    @Test
    public void execute_jobseekerWithoutTags_noChange() {
        model.addJobseeker(new JobseekerBuilder(jobseeker).withTags().build());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"))), model,
                UntagJobseekerCommand.MESSAGE_NO_CHANGE, expectedModel);
    }

    @Test
    public void execute_filteredList_updatesDisplayedJobseekerAndPreservesFilter() throws Exception {
        Jobseeker hiddenJobseeker = new JobseekerBuilder().withName("Hidden Person").withTags("java").build();
        model.addJobseeker(hiddenJobseeker);
        model.addJobseeker(jobseeker);
        model.updateFilteredJobseekerList(person -> person.getName().equals(jobseeker.getName()));

        new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"))).execute(model);

        Jobseeker updated = new JobseekerBuilder(jobseeker).withTags("teamwork").build();
        assertEquals(List.of(updated), model.getFilteredJobseekerList());
        assertEquals(List.of(hiddenJobseeker, updated), model.getAddressBook().getJobseekerList());
        assertCommandFailure(new UntagJobseekerCommand(INDEX_SECOND_PERSON, Set.of(new Tag("java"))), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        assertCommandFailure(new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"))), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        model.addJobseeker(jobseeker);
        assertCommandFailure(new UntagJobseekerCommand(INDEX_SECOND_PERSON, Set.of(new Tag("java"))), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_inputCleared_retainsTags() throws Exception {
        model.addJobseeker(jobseeker);
        Set<Tag> tags = new HashSet<>(Set.of(new Tag("java")));
        UntagJobseekerCommand command = new UntagJobseekerCommand(INDEX_FIRST_PERSON, tags);
        tags.clear();

        command.execute(model);

        assertEquals(Set.of(new Tag("teamwork")), model.getFilteredJobseekerList().get(0).getTags());
    }

    @Test
    public void equals() {
        Set<Tag> tags = Set.of(new Tag("java"));
        UntagJobseekerCommand command = new UntagJobseekerCommand(INDEX_FIRST_PERSON, tags);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new UntagJobseekerCommand(INDEX_FIRST_PERSON, tags)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
        assertFalse(command.equals(new UntagJobseekerCommand(INDEX_SECOND_PERSON, tags)));
        assertFalse(command.equals(new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("teamwork")))));
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = Set.of(new Tag("java"));
        UntagJobseekerCommand command = new UntagJobseekerCommand(INDEX_FIRST_PERSON, tags);
        String expected = UntagJobseekerCommand.class.getCanonicalName() + "{targetIndex=" + INDEX_FIRST_PERSON
                + ", tagsToRemove=" + tags + "}";
        assertEquals(expected, command.toString());
    }
}
