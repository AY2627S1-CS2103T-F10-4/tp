package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code HideClientCommand}.
 */
public class HideClientCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new HideClientCommand(null));
    }

    /**
     * Hiding must leave the client in the address book, unlike deleting, and take them out of the
     * displayed list. Model equality ignores the displayed list, so that is checked separately.
     */
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Client clientToHide = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        Client hiddenClient = new ClientBuilder(clientToHide).withHidden(true).build();
        HideClientCommand hideCommand = new HideClientCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(HideClientCommand.MESSAGE_HIDE_CLIENT_SUCCESS,
                Messages.format(hiddenClient));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(clientToHide, hiddenClient);

        assertCommandSuccess(hideCommand, model, expectedMessage, expectedModel);
        assertTrue(model.getAddressBook().getClientList().contains(hiddenClient));
        assertTrue(model.getFilteredClientList().stream().noneMatch(clientToHide::isSamePerson));
    }

    /**
     * Hiding the only client in a search result must empty that result, as a search never shows
     * hidden clients.
     */
    @Test
    public void execute_validIndexFilteredList_success() {
        showClientAtIndex(model, INDEX_FIRST_PERSON);

        Client clientToHide = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        Client hiddenClient = new ClientBuilder(clientToHide).withHidden(true).build();
        HideClientCommand hideCommand = new HideClientCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(HideClientCommand.MESSAGE_HIDE_CLIENT_SUCCESS,
                Messages.format(hiddenClient));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(clientToHide, hiddenClient);

        assertCommandSuccess(hideCommand, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredClientList().isEmpty());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        HideClientCommand hideCommand = new HideClientCommand(outOfBoundIndex);

        assertCommandFailure(hideCommand, model, Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showClientAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of the address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getClientList().size());

        HideClientCommand hideCommand = new HideClientCommand(outOfBoundIndex);

        assertCommandFailure(hideCommand, model, Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    /**
     * Hiding a client who is already hidden is not an error, as with tagging a client with a tag
     * they already have, but it must change nothing. The default view never shows hidden clients,
     * so the test shows everyone first, as a view of hidden clients would.
     */
    @Test
    public void execute_clientAlreadyHidden_noChange() {
        Client hiddenClient = new ClientBuilder(ADRIAN).withHidden(true).build();
        Model hiddenModel = new ModelManager();
        hiddenModel.addClient(hiddenClient);
        hiddenModel.updateFilteredClientList(client -> true);
        Model expectedModel = new ModelManager(hiddenModel.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new HideClientCommand(INDEX_FIRST_PERSON), hiddenModel,
                HideClientCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(hiddenClient), hiddenModel.getFilteredClientList());
    }

    @Test
    public void equals() {
        HideClientCommand hideFirstCommand = new HideClientCommand(INDEX_FIRST_PERSON);
        HideClientCommand hideSecondCommand = new HideClientCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(hideFirstCommand.equals(hideFirstCommand));

        // same values -> returns true
        assertTrue(hideFirstCommand.equals(new HideClientCommand(INDEX_FIRST_PERSON)));

        // different types -> returns false
        assertFalse(hideFirstCommand.equals(1));

        // null -> returns false
        assertFalse(hideFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(hideFirstCommand.equals(hideSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        HideClientCommand hideCommand = new HideClientCommand(targetIndex);
        String expected = HideClientCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, hideCommand.toString());
    }
}
