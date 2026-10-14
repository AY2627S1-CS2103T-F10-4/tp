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

public class TagClientCommandTest {

    private final Model model = new ModelManager();
    private final Client client = new ClientBuilder().withTags("finance")
            .withRemark("Contact in the morning").build();

    @Test
    public void execute_multipleNewTags_updatesClientAndPreservesJobseekers() {
        Jobseeker jobseeker = new JobseekerBuilder().withName(client.getName().fullName)
                .withTags("java").build();
        model.addJobseeker(jobseeker);
        model.addClient(client);
        Client updated = new ClientBuilder(client).withTags("finance", "priority", "Priority").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(client, updated);

        assertCommandSuccess(new TagClientCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("priority"), new Tag("Priority"))), model,
                String.format(TagClientCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(List.of(jobseeker), model.getAddressBook().getJobseekerList());
    }

    @Test
    public void execute_clientWithoutTags_addsTags() {
        Client untaggedClient = new ClientBuilder(client).withTags().build();
        model.addClient(untaggedClient);
        Client updated = new ClientBuilder(untaggedClient).withTags("priority", "finance").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(untaggedClient, updated);

        assertCommandSuccess(new TagClientCommand(INDEX_FIRST_PERSON, updated.getTags()), model,
                String.format(TagClientCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        model.addClient(client);
        assertCommandFailure(new TagClientCommand(INDEX_SECOND_PERSON, Set.of(new Tag("priority"))), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagClientCommand(null, Set.of(new Tag("priority"))));
        assertThrows(NullPointerException.class, () -> new TagClientCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_newAndExistingTags_preservesDetailsAndExistingTags() {
        model.addClient(client);
        Client updated = new ClientBuilder(client).withTags("finance", "priority").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(client, updated);

        TagClientCommand command = new TagClientCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("priority"), new Tag("finance")));
        assertCommandSuccess(command, model,
                String.format(TagClientCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(Set.of(new Tag("finance")), client.getTags());
    }

    @Test
    public void execute_existingTags_noChange() {
        model.addClient(client);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new TagClientCommand(INDEX_FIRST_PERSON, client.getTags()), model,
                TagClientCommand.MESSAGE_NO_CHANGE, expectedModel);
    }

    @Test
    public void execute_noChangeFilteredList_preservesFilterAndAllClients() {
        Client hiddenPerson = new ClientBuilder().withName("Hidden Person").build();
        model.addClient(hiddenPerson);
        model.addClient(client);
        model.updateFilteredClientList(person -> person.getName().equals(client.getName()));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new TagClientCommand(INDEX_FIRST_PERSON, client.getTags()), model,
                TagClientCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(client), model.getFilteredClientList());
        assertEquals(List.of(hiddenPerson, client), model.getAddressBook().getClientList());
    }

    @Test
    public void execute_filteredList_updatesDisplayedClientAndPreservesFilter() throws Exception {
        Client hiddenClient = new ClientBuilder().withName("Hidden Person").build();
        model.addClient(hiddenClient);
        model.addClient(client);
        model.updateFilteredClientList(person -> person.getName().equals(client.getName()));

        new TagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"))).execute(model);

        Client updated = new ClientBuilder(client).withTags("finance", "priority").build();
        assertEquals(List.of(updated), model.getFilteredClientList());
        assertEquals(List.of(hiddenClient, updated), model.getAddressBook().getClientList());
        assertCommandFailure(new TagClientCommand(INDEX_SECOND_PERSON, Set.of(new Tag("priority"))), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        assertCommandFailure(new TagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"))), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_inputCleared_retainsTags() throws Exception {
        model.addClient(client);
        Set<Tag> tags = new HashSet<>(Set.of(new Tag("priority")));
        TagClientCommand command = new TagClientCommand(INDEX_FIRST_PERSON, tags);
        tags.clear();

        command.execute(model);

        assertEquals(Set.of(new Tag("priority"), new Tag("finance")),
                model.getFilteredClientList().get(0).getTags());
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = Set.of(new Tag("priority"));
        TagClientCommand command = new TagClientCommand(INDEX_FIRST_PERSON, tags);
        String expected = TagClientCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", tagsToAdd=" + tags + "}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void equals() {
        Set<Tag> tags = Set.of(new Tag("priority"));
        TagClientCommand command = new TagClientCommand(INDEX_FIRST_PERSON, tags);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new TagClientCommand(INDEX_FIRST_PERSON, tags)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
        assertFalse(command.equals(new TagClientCommand(INDEX_SECOND_PERSON, tags)));
        assertFalse(command.equals(new TagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("finance")))));
    }
}
