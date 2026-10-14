package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all jobseekers in the address book whose name, phone number or email contains
 * any of the argument keywords. Keyword matching is case insensitive.
 */
public class FindJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "findj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all jobseekers whose name, "
            + "phone number or email contains any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice 9435 @gmail";

    private final NameContainsKeywordsPredicate predicate;

    public FindJobseekerCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobseekerList(jobseeker -> predicate.test(jobseeker));
        return new CommandResult(
                String.format(Messages.MESSAGE_JOBSEEKERS_LISTED_OVERVIEW, model.getFilteredJobseekerList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindJobseekerCommand otherFindJobseekerCommand)) {
            return false;
        }

        return predicate.equals(otherFindJobseekerCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
