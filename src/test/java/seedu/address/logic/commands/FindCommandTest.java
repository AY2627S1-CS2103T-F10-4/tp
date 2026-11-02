package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.preparePredicate;
import static seedu.address.testutil.TypicalPersons.ADRIAN;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BRENDA;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.DIANA;
import static seedu.address.testutil.TypicalPersons.ETHAN;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Executes a {@code FindCommand} built from {@code userInput} on the typical address book, and
     * checks that exactly {@code expectedJobseekers} and {@code expectedClients} are listed, each in
     * the order given, with the matching result message.
     */
    private void assertFindSuccess(String userInput, List<Jobseeker> expectedJobseekers,
            List<Client> expectedClients) {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedJobseekers.size(), expectedClients.size());
        PersonContainsKeywordsPredicate predicate = preparePredicate(userInput);
        FindCommand command = new FindCommand(predicate);
        // Mirrors FindCommand#execute, which never lists hidden jobseekers or clients.
        expectedModel.updateFilteredJobseekerList(Model.PREDICATE_SHOW_UNHIDDEN_JOBSEEKERS.and(predicate));
        expectedModel.updateFilteredClientList(Model.PREDICATE_SHOW_UNHIDDEN_CLIENTS.and(predicate));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // ModelManager#equals does not compare filtered lists, so check both lists explicitly.
        assertEquals(expectedJobseekers, model.getFilteredJobseekerList());
        assertEquals(expectedClients, model.getFilteredClientList());
    }

    /**
     * Executes a {@code FindCommand} built from {@code userInput} on {@code customModel}, for test cases
     * that need people the typical address book does not have, and checks that exactly
     * {@code expectedJobseekers} and {@code expectedClients} are listed, each in the order given, with
     * the matching result message.
     */
    private void assertFindSuccess(Model customModel, String userInput, List<Jobseeker> expectedJobseekers,
            List<Client> expectedClients) {
        CommandResult result = new FindCommand(preparePredicate(userInput)).execute(customModel);

        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedJobseekers.size(),
                expectedClients.size()), result.getFeedbackToUser());
        assertEquals(expectedJobseekers, customModel.getFilteredJobseekerList());
        assertEquals(expectedClients, customModel.getFilteredClientList());
    }

    /**
     * Checks that both lists are filtered: {@code "an"} is part of jobseekers "Daniel" and
     * "anna@example.com" (George), and of clients "Adrian", "Diana" and "Ethan".
     */
    @Test
    public void execute_keywordMatchesBothTypes_jobseekersAndClientsFound() {
        assertFindSuccess("an", List.of(DANIEL, GEORGE), List.of(ADRIAN, DIANA, ETHAN));
    }

    /**
     * Checks that multiple keywords apply to both lists: "Alice" only matches a jobseeker and "Brenda"
     * only a client, so each list must show just its own match.
     */
    @Test
    public void execute_multipleKeywords_eachListShowsOwnMatches() {
        assertFindSuccess("Alice Brenda", List.of(ALICE), List.of(BRENDA));
    }

    /**
     * Checks that a second search looks through everyone again rather than only the people the first
     * search listed. The two searches have no one in common, so this fails if the second search narrows
     * either list further instead of replacing the first search's results.
     */
    @Test
    public void execute_secondSearch_replacesFirstSearch() {
        assertFindSuccess("Alice Brenda", List.of(ALICE), List.of(BRENDA));
        assertFindSuccess("Benson Adrian", List.of(BENSON), List.of(ADRIAN));
    }

    /**
     * Checks that both lists end up empty when nobody matches. This fails if {@code find} falls back to
     * showing everyone when a search finds nothing. The other test cases only ever empty one list at a time.
     */
    @Test
    public void execute_keywordMatchesNobody_bothListsEmpty() {
        assertFindSuccess("zzz", List.of(), List.of());
    }

    /**
     * Checks that nobody is listed when there are no keywords to search for.
     * A user cannot trigger this, as the parser rejects blank input, so this test skips the parser:
     * {@code preparePredicate(" ")} gives a predicate with an empty keyword list.
     */
    @Test
    public void execute_zeroKeywords_noPersonFound() {
        assertFindSuccess(" ", List.of(), List.of());
    }

    /**
     * Checks that a tag search narrows the client list to nobody when only jobseekers carry the tag,
     * so this fails if {@code find} left the client list unfiltered.
     */
    @Test
    public void execute_tagOnlyOnJobseekers_noClientFound() {
        assertFindSuccess("friends", List.of(ALICE, BENSON, DANIEL), List.of());
    }

    /**
     * Checks the mirror case of {@link #execute_tagOnlyOnJobseekers_noClientFound()}: a tag held only
     * by clients must empty the jobseeker list, so this fails if the jobseeker list were left alone.
     */
    @Test
    public void execute_tagOnlyOnClients_noJobseekerFound() {
        assertFindSuccess("partner", List.of(), List.of(BRENDA, DIANA));
    }

    /**
     * Checks that {@code find} lists both the jobseeker and the client who have the searched tag.
     * No jobseeker and client in the typical address book share a tag, so this test gives Alice and
     * Adrian the same tag. Benson and Brenda do not have it, so they must not be listed.
     */
    @Test
    public void execute_tagSharedByBothTypes_jobseekersAndClientsFound() {
        Jobseeker taggedJobseeker = new JobseekerBuilder(ALICE).withTags("technology").build();
        Client taggedClient = new ClientBuilder(ADRIAN).withTags("technology").build();
        Model taggedModel = new ModelManager();
        taggedModel.addJobseeker(taggedJobseeker);
        taggedModel.addJobseeker(BENSON);
        taggedModel.addClient(taggedClient);
        taggedModel.addClient(BRENDA);

        assertFindSuccess(taggedModel, "technology", List.of(taggedJobseeker), List.of(taggedClient));
    }

    /**
     * Checks that {@code find} searches clients' job openings: {@code "engineer"} matches only the job
     * opening of the client, so this fails if job openings are not searched. The jobseeker list must
     * be empty, since jobseekers have no job openings.
     */
    @Test
    public void execute_keywordMatchesClientJob_clientFound() {
        Client clientWithJob = new ClientBuilder(ADRIAN).withAvailableJobs("Software Engineer").build();
        Model jobModel = new ModelManager();
        jobModel.addJobseeker(ALICE);
        jobModel.addClient(clientWithJob);
        jobModel.addClient(BRENDA);

        assertFindSuccess(jobModel, "engineer", List.of(), List.of(clientWithJob));
    }

    /**
     * Checks that {@code find} searches jobseekers' qualifications and clients' job openings in one
     * command: {@code "degree"} matches only Alice's qualification and {@code "engineer"} only Adrian's
     * job opening.
     */
    @Test
    public void execute_qualificationAndJobKeywords_jobseekerAndClientFound() {
        Jobseeker jobseekerWithDegree = new JobseekerBuilder(ALICE)
                .withHighestQualification("Degree in Computing").build();
        Client clientWithJob = new ClientBuilder(ADRIAN).withAvailableJobs("Software Engineer").build();
        Model mixedModel = new ModelManager();
        mixedModel.addJobseeker(jobseekerWithDegree);
        mixedModel.addJobseeker(BENSON);
        mixedModel.addClient(clientWithJob);
        mixedModel.addClient(BRENDA);

        assertFindSuccess(mixedModel, "degree engineer", List.of(jobseekerWithDegree),
                List.of(clientWithJob));
    }

    /**
     * Hidden jobseekers and clients must not be found, even though the keywords match them. This is
     * the same rule that {@code findj} and {@code findc} follow.
     */
    @Test
    public void execute_keywordMatchesHiddenPeople_hiddenPeopleNotFound() {
        Model hiddenModel = new ModelManager();
        hiddenModel.addJobseeker(ALICE);
        hiddenModel.addJobseeker(new JobseekerBuilder(BENSON).withHidden(true).build());
        hiddenModel.addClient(ADRIAN);
        hiddenModel.addClient(new ClientBuilder(BRENDA).withHidden(true).build());

        assertFindSuccess(hiddenModel, "Alice Benson Adrian Brenda", List.of(ALICE), List.of(ADRIAN));
    }
}
