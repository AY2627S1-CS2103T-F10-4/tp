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
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class TagJobseekerCommandTest {

    private final Model model = new ModelManager();
    private final Jobseeker jobseeker = new JobseekerBuilder().withTags("teamwork")
            .withRemark("Available immediately").withEmail("unique-email@example.com").build();

    @Test
    public void execute_multipleNewTags_updatesJobseekerAndPreservesClients() {
        Client client = new ClientBuilder().withName(jobseeker.getName().fullName)
                .withTags("finance").build();
        model.addClient(client);
        model.addJobseeker(jobseeker);
        Jobseeker updated = new JobseekerBuilder(jobseeker).withTags("teamwork", "java", "Java").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(jobseeker, updated);

        assertCommandSuccess(new TagJobseekerCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("java"), new Tag("Java"))), model,
                String.format(TagJobseekerCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(List.of(client), model.getAddressBook().getClientList());
    }

    @Test
    public void execute_jobseekerWithoutTags_addsTags() {
        Jobseeker untaggedJobseeker = new JobseekerBuilder(jobseeker).withTags().build();
        model.addJobseeker(untaggedJobseeker);
        Jobseeker updated = new JobseekerBuilder(untaggedJobseeker).withTags("java", "teamwork").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(untaggedJobseeker, updated);

        assertCommandSuccess(new TagJobseekerCommand(INDEX_FIRST_PERSON, updated.getTags()), model,
                String.format(TagJobseekerCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        model.addJobseeker(jobseeker);
        assertCommandFailure(new TagJobseekerCommand(INDEX_SECOND_PERSON, Set.of(new Tag("java"))), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagJobseekerCommand(null, Set.of(new Tag("java"))));
        assertThrows(NullPointerException.class, () -> new TagJobseekerCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_newAndExistingTags_preservesDetailsAndExistingTags() {
        model.addJobseeker(jobseeker);
        Jobseeker updated = new JobseekerBuilder(jobseeker).withTags("teamwork", "java").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setJobseeker(jobseeker, updated);

        TagJobseekerCommand command = new TagJobseekerCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("java"), new Tag("teamwork")));
        assertCommandSuccess(command, model,
                String.format(TagJobseekerCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(Set.of(new Tag("teamwork")), jobseeker.getTags());
    }

    @Test
    public void execute_existingTags_noChange() {
        model.addJobseeker(jobseeker);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new TagJobseekerCommand(INDEX_FIRST_PERSON, jobseeker.getTags()), model,
                TagJobseekerCommand.MESSAGE_NO_CHANGE, expectedModel);
    }

    @Test
    public void execute_noChangeFilteredList_preservesFilterAndAllJobseekers() {
        Jobseeker hiddenPerson = new JobseekerBuilder().withName("Hidden Person").build();
        model.addJobseeker(hiddenPerson);
        model.addJobseeker(jobseeker);
        model.updateFilteredJobseekerList(person -> person.getName().equals(jobseeker.getName()));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new TagJobseekerCommand(INDEX_FIRST_PERSON, jobseeker.getTags()), model,
                TagJobseekerCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(jobseeker), model.getFilteredJobseekerList());
        assertEquals(List.of(hiddenPerson, jobseeker), model.getAddressBook().getJobseekerList());
    }

    @Test
    public void execute_filteredList_updatesDisplayedJobseekerAndPreservesFilter() throws Exception {
        Jobseeker hiddenJobseeker = new JobseekerBuilder().withName("Hidden Person").build();
        model.addJobseeker(hiddenJobseeker);
        model.addJobseeker(jobseeker);
        model.updateFilteredJobseekerList(person -> person.getName().equals(jobseeker.getName()));

        new TagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"))).execute(model);

        Jobseeker updated = new JobseekerBuilder(jobseeker).withTags("teamwork", "java").build();
        assertEquals(List.of(updated), model.getFilteredJobseekerList());
        assertEquals(List.of(hiddenJobseeker, updated), model.getAddressBook().getJobseekerList());
        assertCommandFailure(new TagJobseekerCommand(INDEX_SECOND_PERSON, Set.of(new Tag("java"))), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        assertCommandFailure(new TagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"))), model,
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_inputCleared_retainsTags() throws Exception {
        model.addJobseeker(jobseeker);
        Set<Tag> tags = new HashSet<>(Set.of(new Tag("java")));
        TagJobseekerCommand command = new TagJobseekerCommand(INDEX_FIRST_PERSON, tags);
        tags.clear();

        command.execute(model);

        assertEquals(Set.of(new Tag("java"), new Tag("teamwork")),
                model.getFilteredJobseekerList().get(0).getTags());
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = Set.of(new Tag("java"));
        TagJobseekerCommand command = new TagJobseekerCommand(INDEX_FIRST_PERSON, tags);
        String expected = TagJobseekerCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", tagsToAdd=" + tags + "}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void equals() {
        Set<Tag> tags = Set.of(new Tag("java"));
        TagJobseekerCommand command = new TagJobseekerCommand(INDEX_FIRST_PERSON, tags);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new TagJobseekerCommand(INDEX_FIRST_PERSON, tags)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
        assertFalse(command.equals(new TagJobseekerCommand(INDEX_SECOND_PERSON, tags)));
        assertFalse(command.equals(new TagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("teamwork")))));
    }

    /**
     * Tagging rebuilds the jobseeker, so it must carry the hidden status over rather than unhiding them.
     */
    @Test
    public void execute_hiddenJobseeker_staysHidden() throws Exception {
        Jobseeker hiddenJobseeker = new JobseekerBuilder(jobseeker).withHidden(true).build();
        model.addJobseeker(hiddenJobseeker);
        new TagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"))).execute(model);
        assertTrue(model.getFilteredJobseekerList().get(0).isHidden());
    }
}
