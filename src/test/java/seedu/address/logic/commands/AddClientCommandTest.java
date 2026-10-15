package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Client;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/** Contains unit tests for {@link AddClientCommand}. */
public class AddClientCommandTest {
    @Test
    public void constructor_nullClient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddClientCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AddClientCommand command = new AddClientCommand(new ClientBuilder().build());
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        Client alice = new ClientBuilder().withName("Alice").withRemark("Hiring engineers").build();
        AddClientCommand command = new AddClientCommand(alice);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new AddClientCommand(new ClientBuilder(alice).build())));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
        assertFalse(command.equals(new AddJobseekerCommand(new JobseekerBuilder().build())));
        assertFalse(command.equals(new AddClientCommand(new ClientBuilder(alice).withName("Bob").build())));
        assertFalse(command.equals(new AddClientCommand(new ClientBuilder(alice).withRemark("On hold").build())));
    }

    @Test
    public void toStringMethod() {
        Client client = new ClientBuilder().withRemark("Hiring engineers").build();
        AddClientCommand command = new AddClientCommand(client);
        assertEquals(AddClientCommand.class.getCanonicalName() + "{toAdd=" + client + "}", command.toString());
    }
}
