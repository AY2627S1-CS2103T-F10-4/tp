package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.Messages.MESSAGE_JOBSEEKERS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.preparePredicate;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalClients;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindJobseekerCommand}.
 */
public class FindJobseekerCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Executes a {@code FindJobseekerCommand} built from {@code userInput} on the typical address book,
     * and checks that exactly {@code expectedJobseekers} are listed, in that order, with the matching
     * result message.
     */
    private void assertFindSuccess(String userInput, List<Jobseeker> expectedJobseekers) {
        String expectedMessage = String.format(MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, expectedJobseekers.size());
        PersonContainsKeywordsPredicate predicate = preparePredicate(userInput);
        FindJobseekerCommand command = new FindJobseekerCommand(predicate);
        expectedModel.updateFilteredJobseekerList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedJobseekers, model.getFilteredJobseekerList());
    }

    /**
     * Executes a {@code FindJobseekerCommand} built from {@code userInput} on {@code customModel}, for
     * test cases that need jobseekers the typical address book does not have, and checks that exactly
     * {@code expectedJobseekers} are listed, with the matching result message.
     */
    private void assertFindSuccess(Model customModel, String userInput, List<Jobseeker> expectedJobseekers) {
        CommandResult result = new FindJobseekerCommand(preparePredicate(userInput)).execute(customModel);

        assertEquals(String.format(MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, expectedJobseekers.size()),
                result.getFeedbackToUser());
        assertEquals(expectedJobseekers, customModel.getFilteredJobseekerList());
    }

    /**
     * Checks that no jobseeker is listed when there are no keywords to search for.
     * A user cannot trigger this, as the parser rejects blank input, so this test skips the parser:
     * {@code preparePredicate(" ")} gives a predicate with an empty keyword list.
     */
    @Test
    public void execute_zeroKeywords_noPersonFound() {
        assertFindSuccess(" ", List.of());
    }

    /**
     * Checks that a jobseeker is listed if they match any one of the keywords, rather than all of them.
     * Each keyword here matches a different jobseeker: Carl Kurz, Elle Meyer and Fiona Kunz.
     */
    @Test
    public void execute_multipleNameKeywords_personsFound() {
        assertFindSuccess("Kurz Elle Kunz", List.of(CARL, ELLE, FIONA));
    }

    /**
     * Checks that a keyword matching only part of a jobseeker's name still finds those jobseekers.
     * {@code "eier"} is not a whole word in any name but lies inside "Meier", so exactly
     * Benson Meier and Daniel Meier are listed. This test case guards substring matching: it fails
     * if {@code findj} reverts to matching whole words only.
     */
    @Test
    public void execute_partialNameKeyword_personsFound() {
        assertFindSuccess("eier", List.of(BENSON, DANIEL));
    }

    /**
     * Checks that a jobseeker whose name is close to the keyword, but does not contain it, is not listed.
     * Elle Meyer is one letter away from "Meier" and does not contain {@code "eier"}, so this test case
     * fails if matching is loosened, for example to fuzzy or typo-tolerant matching.
     * On its own, this test case would also pass if nothing were found, so it relies on
     * {@link #execute_partialNameKeyword_personsFound()} to check that matching still works.
     */
    @Test
    public void execute_partialNameKeyword_nearMissNotFound() {
        FindJobseekerCommand command = new FindJobseekerCommand(preparePredicate("eier"));
        command.execute(model);
        assertFalse(model.getFilteredJobseekerList().contains(ELLE));
    }

    /**
     * Checks that phone numbers are searched, and that part of a number is enough to match.
     * {@code "9435"} appears only in Alice's phone number, 94351253.
     */
    @Test
    public void execute_partialPhoneKeyword_personFound() {
        assertFindSuccess("9435", List.of(ALICE));
    }

    /**
     * Checks that email addresses are searched. {@code "cornelia"} appears in no name, only in
     * Daniel's email address, cornelia@example.com.
     */
    @Test
    public void execute_partialEmailKeyword_personFound() {
        assertFindSuccess("cornelia", List.of(DANIEL));
    }

    /**
     * Checks that tags are searched: {@code "owes"} appears only in Benson's tag "owesMoney".
     */
    @Test
    public void execute_partialTagKeyword_personFound() {
        assertFindSuccess("owes", List.of(BENSON));
    }

    /**
     * Checks that {@code findj} leaves the client list alone. {@code ModelManager#equals} does not
     * compare filtered lists, so {@code assertCommandSuccess} alone would not notice if this command
     * filtered clients by mistake. {@code "Adrian"} matches a client but no jobseeker, so the jobseeker
     * list must end up empty, which it would not if this command filtered the client list instead.
     */
    @Test
    public void execute_clientKeyword_clientListUnchanged() {
        assertFindSuccess("Adrian", List.of());
        assertEquals(getTypicalClients(), model.getFilteredClientList());
    }

    /**
     * Checks that a jobseeker's highest qualification is searched: {@code "diploma"} matches only
     * Benson's qualification, so this fails if qualifications are not searched.
     */
    @Test
    public void execute_qualificationKeyword_jobseekerFound() {
        Jobseeker bensonWithDiploma = new JobseekerBuilder(BENSON).withHighestQualification("Diploma in IT")
                .build();
        Model qualificationModel = new ModelManager();
        qualificationModel.addJobseeker(ALICE);
        qualificationModel.addJobseeker(bensonWithDiploma);

        assertFindSuccess(qualificationModel, "diploma", List.of(bensonWithDiploma));
    }

    /**
     * A hidden jobseeker must not be found, even though the keywords match them.
     */
    @Test
    public void execute_keywordMatchesHiddenJobseeker_hiddenJobseekerNotFound() {
        Model hiddenModel = new ModelManager();
        hiddenModel.addJobseeker(ALICE);
        hiddenModel.addJobseeker(new JobseekerBuilder(BENSON).withHidden(true).build());

        assertFindSuccess(hiddenModel, "Alice Benson", List.of(ALICE));
    }
}
