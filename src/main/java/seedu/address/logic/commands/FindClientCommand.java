package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all unhidden clients in the address book whose name, phone number or email
 * contains any of the argument keywords. Keyword matching is case insensitive. Hidden clients
 * and jobseekers are not searched.
 */
public class FindClientCommand extends Command {

    public static final String COMMAND_WORD = "findc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all clients whose name, "
            + "phone number or email contains any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " adrian 81110001 adrian@example.com";

    private static final Logger logger = LogsCenter.getLogger(FindClientCommand.class);

    private final PersonContainsKeywordsPredicate predicate;

    /**
     * Creates a command that lists all clients that match {@code predicate}.
     *
     * @param predicate Predicate that decides which clients are listed; must not be null.
     */
    public FindClientCommand(PersonContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_UNHIDDEN_CLIENTS.and(predicate));
        int matchCount = model.getFilteredClientList().size();
        // FINE, not INFO: LogicManager already logs the command text at INFO, so this only adds detail
        // that is useful when debugging search results.
        logger.fine("Found " + matchCount + " client(s) matching " + predicate);
        return new CommandResult(String.format(Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW, matchCount));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindClientCommand otherFindClientCommand)) {
            return false;
        }

        return predicate.equals(otherFindClientCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
