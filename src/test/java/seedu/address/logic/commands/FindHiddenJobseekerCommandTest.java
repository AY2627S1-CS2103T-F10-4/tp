package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_HIDDEN_JOBSEEKERS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.preparePredicate;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindHiddenJobseekerCommand}.
 * Keyword matching itself (partial words, phone numbers, emails) is shared with {@code findj} and
 * covered by {@code FindJobseekerCommandTest}.
 */
public class FindHiddenJobseekerCommandTest {

    private final Jobseeker hiddenBenson = new JobseekerBuilder(BENSON).withHidden(true).build();
    private final Jobseeker hiddenCarl = new JobseekerBuilder(CARL).withHidden(true).build();

    /**
     * Returns a model with Alice, who is unhidden, and Benson and Carl, who are hidden.
     */
    private Model prepareModel() {
        Model model = new ModelManager();
        model.addJobseeker(ALICE);
        model.addJobseeker(hiddenBenson);
        model.addJobseeker(hiddenCarl);
        return model;
    }

    /**
     * Executes a {@code FindHiddenJobseekerCommand} built from {@code userInput} on {@code model}, and
     * checks that exactly {@code expectedJobseekers} are listed, in that order, with the matching result
     * message.
     */
    private void assertFindSuccess(Model model, String userInput, List<Jobseeker> expectedJobseekers) {
        CommandResult result = new FindHiddenJobseekerCommand(preparePredicate(userInput)).execute(model);

        assertEquals(String.format(MESSAGE_HIDDEN_JOBSEEKERS_LISTED_OVERVIEW, expectedJobseekers.size()),
                result.getFeedbackToUser());
        assertEquals(expectedJobseekers, model.getFilteredJobseekerList());
    }

    /**
     * Checks that no hidden jobseeker is listed when there are no keywords to search for.
     * A user cannot trigger this, as the parser rejects blank input, so this test skips the parser:
     * {@code preparePredicate(" ")} gives a predicate with an empty keyword list.
     */
    @Test
    public void execute_zeroKeywords_noJobseekerFound() {
        assertFindSuccess(prepareModel(), " ", List.of());
    }

    /**
     * An unhidden jobseeker must not be found, even though the keywords match them.
     */
    @Test
    public void execute_keywordsMatchHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "Alice Benson", List.of(hiddenBenson));
    }

    /**
     * Checks that every hidden jobseeker matching any of the keywords is listed.
     * {@code "Benson"} and {@code "Carl"} each match a different hidden jobseeker.
     */
    @Test
    public void execute_multipleKeywords_allMatchingHiddenJobseekersFound() {
        assertFindSuccess(prepareModel(), "Benson Carl", List.of(hiddenBenson, hiddenCarl));
    }

    /**
     * Alice and Benson are both tagged "friends", but only Benson is hidden, so a tag search must
     * still leave out unhidden jobseekers.
     */
    @Test
    public void execute_tagMatchesHiddenAndUnhidden_onlyHiddenFound() {
        assertFindSuccess(prepareModel(), "friends", List.of(hiddenBenson));
    }

    /**
     * Checks that nobody is listed when the keyword matches only an unhidden jobseeker.
     * {@code "Alice"} matches Alice, who is not hidden, so {@code findhj} must not list them.
     */
    @Test
    public void execute_keywordMatchesOnlyUnhidden_noJobseekerFound() {
        assertFindSuccess(prepareModel(), "Alice", List.of());
    }

    /**
     * Checks that a jobseeker hidden with {@code hidej} can then be found with {@code findhj}, which is
     * how a recruiter would use the two commands together.
     */
    @Test
    public void execute_afterHidej_findsHiddenJobseeker() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Jobseeker jobseekerToHide = model.getFilteredJobseekerList().get(INDEX_FIRST_PERSON.getZeroBased());
        new HideJobseekerCommand(INDEX_FIRST_PERSON).execute(model);

        Jobseeker hiddenJobseeker = new JobseekerBuilder(jobseekerToHide).withHidden(true).build();
        assertFindSuccess(model, "Alice", List.of(hiddenJobseeker));
    }

    /**
     * Checks that {@code findhj} searches only the jobseeker list and leaves the client list as it was.
     * The client list starts narrowed down to Brenda, so this fails if the command resets it to show
     * all clients. The keywords match both clients, so this also fails if the command searches the
     * client list by mistake.
     */
    @Test
    public void execute_clientListFiltered_clientListUnchanged() {
        Model model = new ModelManager();
        model.addClient(ADRIAN);
        model.addClient(BRENDA);
        showClientAtIndex(model, INDEX_SECOND_PERSON);

        assertFindSuccess(model, "Adrian Brenda", List.of());
        assertEquals(List.of(BRENDA), model.getFilteredClientList());
    }
}
