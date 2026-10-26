package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;

/**
 * Contains integration tests for CopyClientCommand.
 */
public class CopyClientCommandTest {

    @Test
    public void execute_multipleDisplayedClients_copiesDetailsInDisplayOrder() throws Exception {
        // EP: no index supplied; all displayed clients are copied in display order.
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);

        CommandResult result = new CopyClientCommand().execute(model);

        assertEquals(String.format(CopyClientCommand.MESSAGE_SUCCESS, 2), result.getFeedbackToUser());
        assertEquals(String.join(System.lineSeparator(), Messages.format(ADRIAN), Messages.format(BRENDA)),
                result.getClipboardText());
    }

    @Test
    public void execute_filteredClients_copiesOnlyDisplayedClients() throws Exception {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);
        model.updateFilteredClientList(client -> client.equals(BRENDA));

        CommandResult result = new CopyClientCommand().execute(model);

        assertEquals(String.format(CopyClientCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
        assertEquals(Messages.format(BRENDA), result.getClipboardText());
    }

    @Test
    public void execute_noDisplayedClients_copiesEmptyText() throws Exception {
        // EP: empty displayed list
        Model model = new ModelManager();
        model.updateFilteredClientList(client -> false);

        CommandResult result = new CopyClientCommand().execute(model);

        assertEquals(String.format(CopyClientCommand.MESSAGE_SUCCESS, 0), result.getFeedbackToUser());
        assertEquals("", result.getClipboardText());
    }

    @Test
    public void execute_validDisplayedIndex_copiesSelectedClient() throws Exception {
        // EP: valid displayed index within the unfiltered list
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Client clientToCopy = model.getFilteredClientList().get(INDEX_SECOND_PERSON.getZeroBased());

        CommandResult result = new CopyClientCommand(INDEX_SECOND_PERSON).execute(model);

        assertEquals(CopyClientCommand.MESSAGE_SUCCESS_SINGLE, result.getFeedbackToUser());
        assertEquals(Messages.format(clientToCopy), result.getClipboardText());
    }

    @Test
    public void execute_validDisplayedIndexAfterFiltering_copiesSelectedClient() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showClientAtIndex(model, INDEX_FIRST_PERSON);

        // EP: valid index within a filtered displayed list; indexing is relative to the display.
        CommandResult result = new CopyClientCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(CopyClientCommand.MESSAGE_SUCCESS_SINGLE, result.getFeedbackToUser());
        assertEquals(Messages.format(model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased())),
                result.getClipboardText());
    }

    @Test
    public void execute_indexOutsideDisplayedRange_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // EP: positive index immediately above the displayed-list upper boundary
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        assertCommandFailure(new CopyClientCommand(outOfBoundsIndex), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_indexInAddressBookButOutsideFilteredRange_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showClientAtIndex(model, INDEX_FIRST_PERSON);

        // EP: index valid for the address book but outside the filtered displayed-list partition
        assertCommandFailure(new CopyClientCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }
}
