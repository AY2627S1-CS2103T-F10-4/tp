package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnhideClientCommand}.
 */
public class UnhideClientCommandTest {
    @TempDir
    public Path testFolder;

    private final Model model = new ModelManager();
    private final Client hiddenPerson = new ClientBuilder(ADRIAN).withRemark("Keep this remark")
            .withTags("priority").withHidden(true).build();

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnhideClientCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnhideClientCommand(INDEX_FIRST_PERSON).execute(null));
    }

    /**
     * Restoring from the full hidden list preserves all details and removes the person from that view.
     */
    @Test
    public void execute_hiddenList_success() {
        model.addClient(hiddenPerson);
        new ListHiddenClientCommand().execute(model);
        Client restoredPerson = new ClientBuilder(hiddenPerson).withHidden(false).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(hiddenPerson, restoredPerson);

        assertCommandSuccess(new UnhideClientCommand(INDEX_FIRST_PERSON), model,
                String.format(UnhideClientCommand.MESSAGE_UNHIDE_CLIENT_SUCCESS, Messages.format(restoredPerson)),
                expectedModel);
        assertTrue(model.getFilteredClientList().isEmpty());
        assertEquals(List.of(restoredPerson), model.getAddressBook().getClientList());
    }

    @Test
    public void execute_hiddenPerson_visibleInListAndSearch() throws Exception {
        model.addClient(hiddenPerson);
        new ListHiddenClientCommand().execute(model);
        new UnhideClientCommand(INDEX_FIRST_PERSON).execute(model);
        Client restoredPerson = new ClientBuilder(hiddenPerson).withHidden(false).build();

        new ListClientCommand().execute(model);
        assertEquals(List.of(restoredPerson), model.getFilteredClientList());
        new FindClientCommand(new PersonContainsKeywordsPredicate(List.of(hiddenPerson.getEmail().value)))
                .execute(model);
        assertEquals(List.of(restoredPerson), model.getFilteredClientList());
    }

    @Test
    public void execute_saveAndReload_staysUnhidden() throws Exception {
        model.addClient(hiddenPerson);
        new ListHiddenClientCommand().execute(model);
        new UnhideClientCommand(INDEX_FIRST_PERSON).execute(model);
        Client restoredPerson = new ClientBuilder(hiddenPerson).withHidden(false).build();

        JsonAddressBookStorage storage = new JsonAddressBookStorage(testFolder.resolve("addressbook.json"));
        storage.saveAddressBook(model.getAddressBook());
        Model reloadedModel = new ModelManager(storage.readAddressBook().orElseThrow(), new UserPrefs());
        assertEquals(List.of(restoredPerson), reloadedModel.getFilteredClientList());
    }

    /**
     * Restoring the only search result uses its displayed index and keeps other hidden people filtered out.
     */
    @Test
    public void execute_hiddenSearch_success() {
        Client otherPerson = new ClientBuilder().withName("Other Person").withPhone("88887777")
                .withEmail("other@example.com").withHidden(true).build();
        model.addClient(otherPerson);
        model.addClient(hiddenPerson);
        new FindHiddenClientCommand(new PersonContainsKeywordsPredicate(List.of(hiddenPerson.getEmail().value)))
                .execute(model);

        Client restoredPerson = new ClientBuilder(hiddenPerson).withHidden(false).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setClient(hiddenPerson, restoredPerson);
        String expectedMessage = String.format(UnhideClientCommand.MESSAGE_UNHIDE_CLIENT_SUCCESS,
                Messages.format(restoredPerson));

        assertCommandSuccess(new UnhideClientCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredClientList().isEmpty());
        assertEquals(List.of(otherPerson, restoredPerson),
                model.getAddressBook().getClientList());
        new ListHiddenClientCommand().execute(model);
        assertEquals(List.of(otherPerson), model.getFilteredClientList());
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        assertCommandFailure(new UnhideClientCommand(INDEX_FIRST_PERSON), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidListIndex_throwsCommandException() {
        model.addClient(hiddenPerson);
        new ListHiddenClientCommand().execute(model);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        assertCommandFailure(new UnhideClientCommand(outOfBoundIndex), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    /**
     * An index within the address book can still be outside the displayed search results.
     */
    @Test
    public void execute_invalidSearchIndex_throwsCommandException() {
        model.addClient(hiddenPerson);
        model.addClient(new ClientBuilder().withPhone("88887777").withEmail("other@example.com")
                .withHidden(true).build());
        new FindHiddenClientCommand(new PersonContainsKeywordsPredicate(List.of(hiddenPerson.getEmail().value)))
                .execute(model);
        assertTrue(INDEX_SECOND_PERSON.getZeroBased() < model.getAddressBook().getClientList().size());

        assertCommandFailure(new UnhideClientCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyUnhidden_noChange() {
        model.addClient(ADRIAN);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new UnhideClientCommand(INDEX_FIRST_PERSON), model,
                UnhideClientCommand.MESSAGE_NO_CHANGE, expectedModel);
        assertEquals(List.of(ADRIAN), model.getFilteredClientList());
    }

    @Test
    public void equals() {
        UnhideClientCommand command = new UnhideClientCommand(INDEX_FIRST_PERSON);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new UnhideClientCommand(INDEX_FIRST_PERSON)));
        assertFalse(command.equals(new UnhideClientCommand(INDEX_SECOND_PERSON)));
        assertFalse(command.equals(new HideClientCommand(INDEX_FIRST_PERSON)));
        assertFalse(command.equals(null));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = INDEX_FIRST_PERSON;
        UnhideClientCommand command = new UnhideClientCommand(targetIndex);
        String expected = UnhideClientCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, command.toString());
    }
}
