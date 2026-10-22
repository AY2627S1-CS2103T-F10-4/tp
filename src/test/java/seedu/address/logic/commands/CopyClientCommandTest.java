package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.BRENDA;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains integration tests for CopyClientCommand.
 */
public class CopyClientCommandTest {

    @Test
    public void execute_multipleDisplayedClients_copiesDetailsInDisplayOrder() {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);

        CommandResult result = new CopyClientCommand().execute(model);

        assertEquals(String.format(CopyClientCommand.MESSAGE_SUCCESS, 2), result.getFeedbackToUser());
        assertEquals(String.join(System.lineSeparator(), Messages.format(ADRIAN), Messages.format(BRENDA)),
                result.getClipboardText());
    }

    @Test
    public void execute_filteredClients_copiesOnlyDisplayedClients() {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);
        model.updateFilteredClientList(client -> client.equals(BRENDA));

        CommandResult result = new CopyClientCommand().execute(model);

        assertEquals(String.format(CopyClientCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
        assertEquals(Messages.format(BRENDA), result.getClipboardText());
    }

    @Test
    public void execute_noDisplayedClients_copiesEmptyText() {
        Model model = new ModelManager();
        model.updateFilteredClientList(client -> false);

        CommandResult result = new CopyClientCommand().execute(model);

        assertEquals(String.format(CopyClientCommand.MESSAGE_SUCCESS, 0), result.getFeedbackToUser());
        assertEquals("", result.getClipboardText());
    }
}
