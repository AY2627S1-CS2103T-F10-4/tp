package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all unhidden jobseekers in the address book whose name, phone number, email or
 * tags contain any of the argument keywords. Keyword matching is case insensitive. Hidden jobseekers
 * and clients are not searched.
 */
public class FindJobseekerCommand extends AbstractFindCommand {

    public static final String COMMAND_WORD = "findj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all jobseekers whose name, "
            + "phone number, email or tags contain any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: <keyword>...\n"
            + "Example: " + COMMAND_WORD + " alice 94351253 alice@gmail.com";

    private static final Logger logger = LogsCenter.getLogger(FindJobseekerCommand.class);

    /**
     * Creates a command that lists all jobseekers that match {@code predicate}.
     *
     * @param predicate Predicate that decides which jobseekers are listed; must not be null.
     */
    public FindJobseekerCommand(PersonContainsKeywordsPredicate predicate) {
        super(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobseekerList(Model.PREDICATE_SHOW_UNHIDDEN_JOBSEEKERS.and(predicate));
        int matchCount = model.getFilteredJobseekerList().size();
        // FINE, not INFO: LogicManager already logs the command text at INFO, so this only adds detail
        // that is useful when debugging search results.
        logger.fine("Found " + matchCount + " jobseeker(s) matching " + predicate);
        return new CommandResult(String.format(Messages.MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, matchCount));
    }
}
