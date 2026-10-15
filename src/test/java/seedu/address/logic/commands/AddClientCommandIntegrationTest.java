package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/** Tests adding clients through the real model, including duplicate detection and remarks. */
public class AddClientCommandIntegrationTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newClientWithRemark_success() {
        Client client = new ClientBuilder().withRemark("Hiring engineers").withTags("employer").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addClient(client);

        assertCommandSuccess(new AddClientCommand(client), model,
                "New client added: Amy Bee; Phone: 85355255; Email: amy@gmail.com; "
                        + "Address: 123, Jurong West Ave 6, #08-111; Remark: Hiring engineers; Tags: [employer]",
                expectedModel);
        assertEquals(client, model.getAddressBook().getClientList().getLast());
    }

    @Test
    public void execute_newClientWithoutRemark_success() {
        Client client = new ClientBuilder().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addClient(client);

        assertCommandSuccess(new AddClientCommand(client), model,
                "New client added: Amy Bee; Phone: 85355255; Email: amy@gmail.com; "
                        + "Address: 123, Jurong West Ave 6, #08-111; Remark: ; Tags: ", expectedModel);
    }

    @Test
    public void execute_duplicateClient_throwsCommandException() {
        Client client = new ClientBuilder().withRemark("Hiring engineers").build();
        model.addClient(client);
        assertCommandFailure(new AddClientCommand(client), model, AddClientCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_duplicateClientWithDifferentRemark_throwsCommandException() {
        Client client = new ClientBuilder().withRemark("Hiring engineers").build();
        model.addClient(client);
        Client duplicate = new ClientBuilder(client).withRemark("On hold").build();
        assertCommandFailure(new AddClientCommand(duplicate), model, AddClientCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_clientMatchingJobseeker_success() throws Exception {
        Model emptyModel = new ModelManager();
        emptyModel.addJobseeker(new JobseekerBuilder().build());
        Client client = new ClientBuilder().build();
        new AddClientCommand(client).execute(emptyModel);

        assertEquals(java.util.List.of(client), emptyModel.getAddressBook().getClientList());
        assertEquals(java.util.List.of(new JobseekerBuilder().build()),
                emptyModel.getAddressBook().getJobseekerList());
    }
}
