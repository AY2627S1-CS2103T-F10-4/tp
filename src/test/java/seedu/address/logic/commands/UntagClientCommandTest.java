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

public class UntagClientCommandTest {

    private final Model model = new ModelManager();
    private final Client client = new ClientBuilder().withTags("priority", "finance")
            .withRemark("Contact in the morning").build();

    @Test
    public void execute_multipleTags_updatesClientAndPreservesJobseekers() {
        Jobseeker jobseeker = new JobseekerBuilder().withName(client.getName().fullName)
                .withTags("priority", "finance").build();
        Client target = new ClientBuilder(client).withTags("priority", "finance", "Priority").build();
        model.addJobseeker(jobseeker);
        model.addClient(target);
        Client updated = new ClientBuilder(target).withTags("Priority").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(target, updated);

        assertCommandSuccess(new UntagClientCommand(INDEX_FIRST_PERSON, client.getTags()), model,
                String.format(UntagClientCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(List.of(jobseeker), model.getAddressBook().getJobseekerList());
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UntagClientCommand(null, Set.of(new Tag("priority"))));
        assertThrows(NullPointerException.class, () -> new UntagClientCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_existingAndMissingTags_preservesDetailsAndOtherTags() {
        model.addClient(client);
        Client updated = new ClientBuilder(client).withTags("finance").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(client, updated);

        UntagClientCommand command = new UntagClientCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("priority"), new Tag("missing")));
        assertCommandSuccess(command, model,
                String.format(UntagClientCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
        assertEquals(Set.of(new Tag("priority"), new Tag("finance")), client.getTags());
    }

    @Test
    public void execute_allTags_removesAllTags() {
        model.addClient(client);
        Client updated = new ClientBuilder(client).withTags().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(client, updated);

        assertCommandSuccess(new UntagClientCommand(INDEX_FIRST_PERSON, client.getTags()), model,
                String.format(UntagClientCommand.MESSAGE_SUCCESS, Messages.format(updated)), expectedModel);
    }

    @Test
    public void execute_missingTags_noChange() {
        model.addClient(client);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("Priority"))), model,
                UntagClientCommand.MESSAGE_NO_CHANGE, expectedModel);
    }

    @Test
    public void execute_clientWithoutTags_noChange() {
        model.addClient(new ClientBuilder(client).withTags().build());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"))), model,
                UntagClientCommand.MESSAGE_NO_CHANGE, expectedModel);
    }

    @Test
    public void execute_noChangeFilteredList_preservesFilterAndAllClients() {
        Client hiddenPerson = new ClientBuilder().withName("Hidden Person")
                .withPhone("91234567").withEmail("hidden@example.com").build();
        model.addClient(hiddenPerson);
        model.addClient(client);
        model.updateFilteredClientList(person -> person.getName().equals(client.getName()));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("missing"))), model,
                UntagClientCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(client), model.getFilteredClientList());
        assertEquals(List.of(hiddenPerson, client), model.getAddressBook().getClientList());
    }

    @Test
    public void execute_filteredList_updatesDisplayedClientAndPreservesFilter() throws Exception {
        Client hiddenClient = new ClientBuilder().withName("Hidden Person")
                .withPhone("91234567").withEmail("hidden@example.com").withTags("priority").build();
        model.addClient(hiddenClient);
        model.addClient(client);
        model.updateFilteredClientList(person -> person.getName().equals(client.getName()));

        new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"))).execute(model);

        Client updated = new ClientBuilder(client).withTags("finance").build();
        assertEquals(List.of(updated), model.getFilteredClientList());
        assertEquals(List.of(hiddenClient, updated), model.getAddressBook().getClientList());
        assertCommandFailure(new UntagClientCommand(INDEX_SECOND_PERSON, Set.of(new Tag("priority"))), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        assertCommandFailure(new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"))), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        model.addClient(client);
        assertCommandFailure(new UntagClientCommand(INDEX_SECOND_PERSON, Set.of(new Tag("priority"))), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_inputCleared_retainsTags() throws Exception {
        model.addClient(client);
        Set<Tag> tags = new HashSet<>(Set.of(new Tag("priority")));
        UntagClientCommand command = new UntagClientCommand(INDEX_FIRST_PERSON, tags);
        tags.clear();

        command.execute(model);

        assertEquals(Set.of(new Tag("finance")), model.getFilteredClientList().get(0).getTags());
    }

    @Test
    public void equals() {
        Set<Tag> tags = Set.of(new Tag("priority"));
        UntagClientCommand command = new UntagClientCommand(INDEX_FIRST_PERSON, tags);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new UntagClientCommand(INDEX_FIRST_PERSON, tags)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
        assertFalse(command.equals(new UntagClientCommand(INDEX_SECOND_PERSON, tags)));
        assertFalse(command.equals(new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("finance")))));
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = Set.of(new Tag("priority"));
        UntagClientCommand command = new UntagClientCommand(INDEX_FIRST_PERSON, tags);
        String expected = UntagClientCommand.class.getCanonicalName() + "{targetIndex=" + INDEX_FIRST_PERSON
                + ", tagsToRemove=" + tags + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * Untagging rebuilds the client, so it must carry the hidden status over rather than unhiding them.
     */
    @Test
    public void execute_hiddenClient_staysHidden() throws Exception {
        Client hiddenClient = new ClientBuilder(client).withHidden(true).build();
        model.addClient(hiddenClient);
        new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"))).execute(model);
        assertTrue(model.getFilteredClientList().get(0).isHidden());
    }
}
