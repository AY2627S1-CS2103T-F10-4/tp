package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.Remark;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class EditClientCommandTest {
    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EditClientCommand(null, new EditClientDescriptor()));
        assertThrows(NullPointerException.class, () -> new EditClientCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        EditClientCommand command = new EditClientCommand(INDEX_FIRST_PERSON, new EditClientDescriptor());
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_allFields_updatesSecondClient() throws Exception {
        Model model = new ModelManager();
        Client first = new ClientBuilder().build();
        Client second = new ClientBuilder().withPhone("91234567").withEmail("second@example.com").build();
        model.addClient(first);
        model.addClient(second);
        Client expected = new ClientBuilder().withName("New Client").withPhone("87654321")
                .withEmail("new@example.com").withAddress("New address").withTags("priority")
                .withRemark("Hiring now").build();
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(expected.getName());
        descriptor.setPhone(expected.getPhone());
        descriptor.setEmail(expected.getEmail());
        descriptor.setAddress(expected.getAddress());
        descriptor.setTags(expected.getTags());
        descriptor.setRemark(expected.getRemark());

        CommandResult result = new EditClientCommand(INDEX_SECOND_PERSON, descriptor).execute(model);

        assertEquals(new CommandResult(String.format(EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(expected))), result);
        assertEquals(List.of(first, expected), model.getFilteredClientList());
    }

    @Test
    public void execute_omittedRemarkAndEmptyTags_preservesRemarkAndClearsTags() throws Exception {
        Model model = new ModelManager();
        Client client = new ClientBuilder().withRemark("Keep this").withTags("priority").build();
        model.addClient(client);
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setTags(Set.of());

        new EditClientCommand(INDEX_FIRST_PERSON, descriptor).execute(model);

        assertEquals(List.of(new ClientBuilder(client).withTags().build()), model.getFilteredClientList());
    }

    @Test
    public void execute_noFields_preservesClient() throws Exception {
        Model model = new ModelManager();
        Client client = new ClientBuilder().withRemark("Keep this").withTags("priority").build();
        model.addClient(client);

        new EditClientCommand(INDEX_FIRST_PERSON, new EditClientDescriptor()).execute(model);

        assertEquals(List.of(client), model.getFilteredClientList());
    }

    @Test
    public void execute_emptyAndUnfilteredOutOfBoundsIndex_leavesModelUnchanged() {
        Model model = new ModelManager();
        assertCommandFailure(new EditClientCommand(INDEX_FIRST_PERSON, new EditClientDescriptor()), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        model.addClient(new ClientBuilder().build());
        assertCommandFailure(new EditClientCommand(INDEX_SECOND_PERSON, new EditClientDescriptor()), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals_comparesClientIndexAndDescriptor() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        EditClientCommand command = new EditClientCommand(INDEX_FIRST_PERSON, descriptor);
        assertEquals(command, command);
        assertEquals(command, new EditClientCommand(INDEX_FIRST_PERSON, descriptor));
        assertNotEquals(command, new EditClientCommand(INDEX_SECOND_PERSON, descriptor));
        descriptor.setRemark(new Remark("Changed"));
        assertNotEquals(command, new EditClientCommand(INDEX_FIRST_PERSON, descriptor));
        assertNotEquals(command, null);
        assertNotEquals(command, new EditJobseekerCommand(INDEX_FIRST_PERSON,
                new EditJobseekerCommand.EditJobseekerDescriptor()));
    }

    @Test
    public void execute_filteredClient_updatesRemarkAndPreservesOtherPeople() throws Exception {
        Model model = new ModelManager();
        Client first = new ClientBuilder().build();
        Client target = new ClientBuilder().withPhone("91234567").withEmail("target@example.com")
                .withRemark("Old remark").withHidden(true).build();
        Jobseeker jobseeker = new JobseekerBuilder().build();
        model.addClient(first);
        model.addClient(target);
        model.addJobseeker(jobseeker);
        model.updateFilteredClientList(client -> client.getEmail().equals(target.getEmail()));

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setRemark(new Remark("New remark"));
        EditClientCommand command = new EditClientCommand(INDEX_FIRST_PERSON, descriptor);
        descriptor.setRemark(new Remark("Changed after construction"));

        Client expected = new ClientBuilder(target).withRemark("New remark").build();
        assertEquals(new CommandResult(String.format(EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(expected))), command.execute(model));
        assertEquals(List.of(first, expected), model.getAddressBook().getClientList());
        assertEquals(List.of(jobseeker), model.getAddressBook().getJobseekerList());
        assertEquals(List.of(expected), model.getFilteredClientList());
    }

    @Test
    public void execute_emptyRemark_clearsRemark() throws Exception {
        Model model = new ModelManager();
        Client client = new ClientBuilder().withRemark("Old remark").build();
        model.addClient(client);
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setRemark(new Remark(""));

        new EditClientCommand(INDEX_FIRST_PERSON, descriptor).execute(model);

        assertEquals(List.of(new ClientBuilder(client).withRemark("").build()),
                model.getAddressBook().getClientList());
    }

    @Test
    public void execute_duplicateClientOrInvalidIndex_leavesModelUnchanged() {
        Model model = new ModelManager();
        Client first = new ClientBuilder().build();
        Client second = new ClientBuilder().withPhone("91234567").withEmail("second@example.com").build();
        model.addClient(first);
        model.addClient(second);
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setPhone(second.getPhone());
        descriptor.setEmail(second.getEmail());

        assertCommandFailure(new EditClientCommand(INDEX_FIRST_PERSON, descriptor), model,
                EditClientCommand.MESSAGE_DUPLICATE_CLIENT);

        model.updateFilteredClientList(client -> client.equals(first));
        assertCommandFailure(new EditClientCommand(INDEX_SECOND_PERSON, descriptor), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @ParameterizedTest(name = "hidden={0}, filtered={1}, newName={2}")
    @CsvSource({
        "false, false, Alice Updated",
        "false, false, Bob Updated",
        "false, true,  Alice Updated",
        "false, true,  Bob Updated",
        "true,  false, Alice Updated",
        "true,  false, Bob Updated",
        "true,  true,  Alice Updated",
        "true,  true,  Bob Updated"
    })
    public void execute_preservesVisibilityAndSearchFilter(
            boolean hidden, boolean filtered, String newName) throws Exception {
        Model viewModel = new ModelManager();
        Client target = new ClientBuilder().withName("Alice Original").withHidden(hidden).build();
        Client other = new ClientBuilder().withName("Charlie Other")
                .withPhone("91234567").withEmail("other@example.com").withHidden(hidden).build();
        Client opposite = new ClientBuilder().withName("Alice Opposite")
                .withPhone("87654321").withEmail("opposite@example.com").withHidden(!hidden).build();
        viewModel.addClient(target);
        viewModel.addClient(other);
        viewModel.addClient(opposite);
        viewModel.updateFilteredClientList(person -> person.isHidden() == hidden
                && (!filtered || new PersonContainsKeywordsPredicate(List.of("Alice")).test(person)));
        EditClientDescriptor descriptor = new EditClientDescriptor();
        Client edited = new ClientBuilder(target).withName(newName).build();
        descriptor.setName(edited.getName());

        new EditClientCommand(INDEX_FIRST_PERSON, descriptor).execute(viewModel);

        List<Client> expected = filtered
                ? (newName.startsWith("Alice") ? List.of(edited) : List.of()) : List.of(edited, other);
        assertEquals(expected, viewModel.getFilteredClientList());
        assertEquals(List.of(edited, other, opposite), viewModel.getAddressBook().getClientList());
    }
}
