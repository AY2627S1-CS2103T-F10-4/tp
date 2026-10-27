package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all hidden jobseekers in the address book whose name, phone number, email or
 * tags contain any of the argument keywords. Keyword matching is case insensitive, as for
 * {@code findj}. Unhidden jobseekers and clients are not searched.
 */
public class FindHiddenJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "findhj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all hidden jobseekers whose name, "
            + "phone number, email or tags contain any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: <keyword>...\n"
            + "Example: " + COMMAND_WORD + " alice 94351253 alice@gmail.com";

    private static final Logger logger = LogsCenter.getLogger(FindHiddenJobseekerCommand.class);

    private final PersonContainsKeywordsPredicate predicate;

    /**
     * Creates a command that lists all hidden jobseekers that match {@code predicate}.
     *
     * @param predicate Predicate that decides which hidden jobseekers are listed; must not be null.
     */
    public FindHiddenJobseekerCommand(PersonContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobseekerList(Model.PREDICATE_SHOW_HIDDEN_JOBSEEKERS.and(predicate));
        int matchCount = model.getFilteredJobseekerList().size();
        // FINE, not INFO: LogicManager already logs the command text at INFO, so this only adds detail
        // that is useful when debugging search results.
        logger.fine("Found " + matchCount + " hidden jobseeker(s) matching " + predicate);
        return new CommandResult(String.format(Messages.MESSAGE_HIDDEN_JOBSEEKERS_LISTED_OVERVIEW,
                matchCount));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindHiddenJobseekerCommand otherFindHiddenJobseekerCommand)) {
            return false;
        }

        return predicate.equals(otherFindHiddenJobseekerCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
