package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all unhidden jobseekers and clients in the address book whose name, phone number,
 * email, tags or (for clients) job openings contain any of the argument keywords. Keyword matching is
 * case insensitive.
 * This has the same effect as running {@code findj} and {@code findc} with the same keywords, so a
 * recruiter can, for example, see every contact with a given tag in one command. Hidden jobseekers
 * and clients are not searched.
 */
public class FindCommand extends AbstractFindCommand {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all jobseekers and clients whose "
            + "name, phone number, email, tags or (for clients) job openings contain any of the specified "
            + "keywords (case-insensitive) and displays them as two lists with index numbers.\n"
            + "Parameters: <keyword>...\n"
            + "Example: " + COMMAND_WORD + " colleagues engineer";

    private static final Logger logger = LogsCenter.getLogger(FindCommand.class);

    /**
     * Creates a command that lists all jobseekers and clients that match {@code predicate}.
     *
     * @param predicate Predicate that decides which jobseekers and clients are listed; must not be null.
     */
    public FindCommand(PersonContainsKeywordsPredicate predicate) {
        super(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // The same predicate filters both lists, which is exactly what findj followed by findc would do.
        model.updateFilteredJobseekerList(Model.PREDICATE_SHOW_UNHIDDEN_JOBSEEKERS.and(predicate));
        model.updateFilteredClientList(Model.PREDICATE_SHOW_UNHIDDEN_CLIENTS.and(predicate));
        int jobseekerMatchCount = model.getFilteredJobseekerList().size();
        int clientMatchCount = model.getFilteredClientList().size();
        // FINE, not INFO: LogicManager already logs the command text at INFO, so this only adds detail
        // that is useful when debugging search results.
        logger.fine("Found " + jobseekerMatchCount + " jobseeker(s) and " + clientMatchCount
                + " client(s) matching " + predicate);
        return new CommandResult(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                jobseekerMatchCount, clientMatchCount));
    }
}
