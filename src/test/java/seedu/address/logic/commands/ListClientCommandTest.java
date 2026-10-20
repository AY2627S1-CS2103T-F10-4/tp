package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListClientCommand.
 */
public class ListClientCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_clientListIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListClientCommand(), model, ListClientCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_clientListIsFiltered_showsEverything() {
        showClientAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListClientCommand(), model, ListClientCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Clients that are hidden must be left out of the list, even when the previous view showed them,
     * but must stay in the address book.
     */
    @Test
    public void execute_hiddenClient_notListed() {
        Client hiddenClient = new ClientBuilder(BRENDA).withHidden(true).build();
        Model hiddenModel = new ModelManager();
        hiddenModel.addClient(ADRIAN);
        hiddenModel.addClient(hiddenClient);
        hiddenModel.updateFilteredClientList(client -> true);

        new ListClientCommand().execute(hiddenModel);

        assertEquals(List.of(ADRIAN), hiddenModel.getFilteredClientList());
        assertEquals(List.of(ADRIAN, hiddenClient), hiddenModel.getAddressBook().getClientList());
    }
}
