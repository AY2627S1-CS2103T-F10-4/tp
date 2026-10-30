package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.preparePredicate;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CEDRIC;
import static seedu.address.testutil.TypicalPersons.DIANA;
import static seedu.address.testutil.TypicalPersons.ETHAN;
import static seedu.address.testutil.TypicalPersons.FELICIA;
import static seedu.address.testutil.TypicalPersons.GAVIN;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalJobseekers;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindClientCommand}.
 */
public class FindClientCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Executes a {@code FindClientCommand} built from {@code userInput} on the typical address book,
     * and checks that exactly {@code expectedClients} are listed, in that order, with the matching
     * result message.
     */
    private void assertFindSuccess(String userInput, List<Client> expectedClients) {
        String expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, expectedClients.size());
        PersonContainsKeywordsPredicate predicate = preparePredicate(userInput);
        FindClientCommand command = new FindClientCommand(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedClients, model.getFilteredClientList());
    }

    /**
     * Executes a {@code FindClientCommand} built from {@code userInput} on {@code customModel}, for test
     * cases that need clients the typical address book does not have, and checks that exactly
     * {@code expectedClients} are listed, with the matching result message.
     */
    private void assertFindSuccess(Model customModel, String userInput, List<Client> expectedClients) {
        CommandResult result = new FindClientCommand(preparePredicate(userInput)).execute(customModel);

        assertEquals(String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, expectedClients.size()),
                result.getFeedbackToUser());
        assertEquals(expectedClients, customModel.getFilteredClientList());
    }

    /**
     * Checks that no client is listed when there are no keywords to search for.
     * A user cannot trigger this, as the parser rejects blank input, so this test skips the parser:
     * {@code preparePredicate(" ")} gives a predicate with an empty keyword list.
     */
    @Test
    public void execute_zeroKeywords_noClientFound() {
        assertFindSuccess(" ", List.of());
    }

    /**
     * Checks that a client is listed if they match any one of the keywords, rather than all of them.
     * Each keyword here matches a different client: Adrian Tan, Brenda Lim and Gavin Koh.
     */
    @Test
    public void execute_multipleNameKeywords_clientsFound() {
        assertFindSuccess("Tan Lim Koh", List.of(ADRIAN, BRENDA, GAVIN));
    }

    /**
     * Checks that substring matching applies to clients too: {@code "an"} is part of
     * "Adrian", "Diana" and "Ethan".
     */
    @Test
    public void execute_partialNameKeyword_clientsFound() {
        assertFindSuccess("an", List.of(ADRIAN, DIANA, ETHAN));
    }

    /**
     * Checks that phone numbers are searched, and that part of a number is enough to match.
     * {@code "0003"} appears only in Cedric's phone number, 81110003.
     */
    @Test
    public void execute_partialPhoneKeyword_clientFound() {
        assertFindSuccess("0003", List.of(CEDRIC));
    }

    /**
     * Checks that email addresses are searched. The name Felicia also contains "felicia", so the
     * keyword includes the {@code "@"}, which appears only in the email address felicia@example.com.
     */
    @Test
    public void execute_partialEmailKeyword_clientFound() {
        assertFindSuccess("felicia@", List.of(FELICIA));
    }

    /**
     * Checks that tags are searched for clients too: only Brenda and Diana are tagged "partner".
     */
    @Test
    public void execute_tagKeyword_clientsFound() {
        assertFindSuccess("partner", List.of(BRENDA, DIANA));
    }

    /**
     * Checks that job openings are searched for clients: {@code "tester"} matches only Cedric's job
     * opening, and {@code "chef"} matches only Diana's.
     */
    @Test
    public void execute_jobKeywords_clientsFound() {
        Client cedricWithJob = new ClientBuilder(CEDRIC).withAvailableJobs("QA Tester").build();
        Client dianaWithJob = new ClientBuilder(DIANA).withAvailableJobs("Head Chef").build();
        Model jobModel = new ModelManager();
        jobModel.addClient(ADRIAN);
        jobModel.addClient(cedricWithJob);
        jobModel.addClient(dianaWithJob);

        assertFindSuccess(jobModel, "tester chef", List.of(cedricWithJob, dianaWithJob));
    }

    /**
     * Checks that {@code findc} leaves the jobseeker list alone. {@code ModelManager#equals} does not
     * compare filtered lists, so {@code assertCommandSuccess} alone would not notice if this command
     * filtered jobseekers by mistake. {@code "Alice"} matches a jobseeker but no client, so the client
     * list must end up empty, which it would not if this command filtered the jobseeker list instead.
     */
    @Test
    public void execute_jobseekerKeyword_jobseekerListUnchanged() {
        assertFindSuccess("Alice", List.of());
        assertEquals(getTypicalJobseekers(), model.getFilteredJobseekerList());
    }

    /**
     * A hidden client must not be found, even though the keywords match them.
     */
    @Test
    public void execute_keywordMatchesHiddenClient_hiddenClientNotFound() {
        Model hiddenModel = new ModelManager();
        hiddenModel.addClient(ADRIAN);
        hiddenModel.addClient(new ClientBuilder(BRENDA).withHidden(true).build());

        assertFindSuccess(hiddenModel, "Adrian Brenda", List.of(ADRIAN));
    }
}
