package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showJobseekerAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CEDRIC;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code ListHiddenClientCommand}.
 */
public class ListHiddenClientCommandTest {

    /**
     * Starts from the default view, which leaves hidden clients out, and checks that the command
     * switches to showing exactly the hidden ones, without changing the address book.
     */
    @Test
    public void execute_someClientsHidden_listsOnlyHiddenClients() {
        Client hiddenBrenda = new ClientBuilder(BRENDA).withHidden(true).build();
        Client hiddenCedric = new ClientBuilder(CEDRIC).withHidden(true).build();
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(hiddenBrenda);
        model.addClient(hiddenCedric);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new ListHiddenClientCommand(), model, ListHiddenClientCommand.MESSAGE_SUCCESS,
                expectedModel);
        assertEquals(List.of(hiddenBrenda, hiddenCedric), model.getFilteredClientList());
    }

    @Test
    public void execute_noClientsHidden_listsNothing() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new ListHiddenClientCommand(), model, ListHiddenClientCommand.MESSAGE_SUCCESS,
                expectedModel);
        assertTrue(model.getFilteredClientList().isEmpty());
    }

    /**
     * Follows what a recruiter does: hide a client with {@code hidec}, then find them again with
     * this command.
     */
    @Test
    public void execute_afterHidec_showsHiddenClient() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Client clientToHide = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        new HideClientCommand(INDEX_FIRST_PERSON).execute(model);

        new ListHiddenClientCommand().execute(model);

        assertEquals(List.of(new ClientBuilder(clientToHide).withHidden(true).build()),
                model.getFilteredClientList());
    }

    /**
     * Only the client list switches to hidden clients; the jobseeker list must keep showing what it
     * showed before. The test starts from a search result rather than the default view, so that it
     * fails on any change to the jobseeker list, including a reset to the default view.
     */
    @Test
    public void execute_jobseekerListFiltered_jobseekerListUnchanged() {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(BENSON);
        showJobseekerAtIndex(model, INDEX_SECOND_PERSON);

        new ListHiddenClientCommand().execute(model);

        assertEquals(List.of(BENSON), model.getFilteredJobseekerList());
    }
}
